package mingzuozhibi.coreserver.security;

import lombok.extern.slf4j.Slf4j;
import mingzuozhibi.coreserver.modules.auth.token.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;

@Slf4j
@WebFilter("/api/*")
@Component
public class SecurityFilter implements Filter {

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    @Transactional
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.getContext();
        if (!(context.getAuthentication() instanceof TokenAuthentication)) {
            trySetContext((HttpServletRequest) request, context);
        }
        chain.doFilter(request, response);
    }

    private void trySetContext(HttpServletRequest request, SecurityContext context) {
        String uuid = request.getHeader("x-token");
        if (uuid != null && !uuid.isEmpty()) {
            tokenRepository.findByUuid(uuid).ifPresent(token -> {
                context.setAuthentication(new TokenAuthentication(token));
            });
        }
    }

}
