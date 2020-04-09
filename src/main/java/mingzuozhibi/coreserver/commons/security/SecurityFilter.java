package mingzuozhibi.coreserver.commons.security;

import mingzuozhibi.coreserver.modules.auth.token.Token;
import mingzuozhibi.coreserver.modules.auth.token.TokenRepository;
import mingzuozhibi.coreserver.modules.auth.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class SecurityFilter implements Filter {

    private Map<User, Authentication> authenticationMap = new ConcurrentHashMap<>();

    public void clearUser(User user) {
        authenticationMap.remove(user);
    }

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    @Transactional
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uuid = ((HttpServletRequest) request).getHeader("x-token");
        if (uuid != null && !uuid.isEmpty()) {
            tokenRepository.findByUuid(uuid).ifPresent(token -> {
                SecurityContext context = SecurityContextHolder.getContext();
                context.setAuthentication(getAuthentication(token));
            });
        }
        chain.doFilter(request, response);
    }

    private Authentication getAuthentication(Token token) {
        return authenticationMap.computeIfAbsent(token.getUser(), MyAuthentication::new);
    }

    private static class MyAuthentication implements Authentication {

        private User user;
        private boolean authenticated;

        public MyAuthentication(User user) {
            this.user = user;
            this.authenticated = true;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return user.getRoles().stream()
                .map(role -> "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        }

        @Override
        public Object getCredentials() {
            return null;
        }

        @Override
        public Object getDetails() {
            return null;
        }

        @Override
        public Object getPrincipal() {
            return null;
        }

        @Override
        public boolean isAuthenticated() {
            return authenticated;
        }

        @Override
        public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
            this.authenticated = isAuthenticated;
        }

        @Override
        public String getName() {
            return user.getUsername();
        }
    }

}
