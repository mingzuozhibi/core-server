package mingzuozhibi.coreserver.security;

import lombok.extern.slf4j.Slf4j;
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
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@WebFilter
@Component
public class SecurityFilter implements Filter {

    @Autowired
    private TokenRepository tokenRepository;

    private Set<Long> userIds = Collections.synchronizedSet(new HashSet<>());

    public void resetUser(User user) {
        userIds.add(user.getId());
        log.debug("resetUser(userId={})", user.getId());
    }

    @Override
    @Transactional
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() instanceof UserAuthentication) {
            User user = ((UserAuthentication) context.getAuthentication()).getDetails();
            if (userIds.remove(user.getId())) {
                log.debug("resetUser(userId={}) reset", user.getId());
                trySetContext((HttpServletRequest) request, context);
            }
        } else {
            trySetContext((HttpServletRequest) request, context);
        }
        chain.doFilter(request, response);
    }

    private void trySetContext(HttpServletRequest request, SecurityContext context) {
        String uuid = request.getHeader("x-token");
        if (uuid != null && !uuid.isEmpty()) {
            log.debug("trySetContext()");
            tokenRepository.findByUuid(uuid).ifPresent(token -> {
                log.debug("trySetContext() beset");
                context.setAuthentication(new UserAuthentication(token.getUser()));
            });
        }
    }

    private static class UserAuthentication implements Authentication {

        private static final long serialVersionUID = 1L;

        private User user;
        private boolean authenticated;

        public UserAuthentication(User user) {
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
        public User getDetails() {
            return user;
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
