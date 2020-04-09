package mingzuozhibi.coreserver.auth.config;

import mingzuozhibi.coreserver.auth.token.Token;
import mingzuozhibi.coreserver.auth.token.TokenRepository;
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

    private static Map<String, Authentication> authenticationMap = new ConcurrentHashMap<>();

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
        return authenticationMap.computeIfAbsent(token.getUuid(), uuid -> new MyAuthentication(token));
    }

    private static class MyAuthentication implements Authentication {

        private final Token token;
        private boolean authenticated;

        public MyAuthentication(Token token) {
            this.token = token;
            this.authenticated = true;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return token.getUser().getRoles().stream()
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
            return token.getUser().getUsername();
        }
    }

}
