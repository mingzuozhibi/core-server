package mingzuozhibi.coreserver.security;

import lombok.extern.slf4j.Slf4j;
import mingzuozhibi.coreserver.modules.auth.user.User;
import mingzuozhibi.coreserver.modules.auth.token.Token;
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

    @Autowired
    private ResetContext resetContext;

    @Override
    @Transactional
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.getContext();
        if (isNeedToReset(context)) {
            trySetContext((HttpServletRequest) request, context);
        }
        chain.doFilter(request, response);
    }

    private boolean isNeedToReset(SecurityContext context) {
        if (!(context.getAuthentication() instanceof TokenAuthentication)) {
            log.debug("发现未认证用户");
            return true;
        }
        Token token = ((TokenAuthentication) context.getAuthentication()).getToken();
        if (resetContext.hasTokenId(token.getId())) {
            log.debug("发现应重置Token: tokenId={}", token.getId());
            return true;
        }
        User user = token.getUser();
        if (resetContext.hasUserId(user.getId())) {
            log.debug("发现应重置Token: userId={}", user.getId());
            return true;
        }
        return false;
    }

    private void trySetContext(HttpServletRequest request, SecurityContext context) {
        String uuid = request.getHeader("x-token");
        if (uuid != null && !uuid.isEmpty()) {
            log.debug("trySetContext()");
            tokenRepository.findByUuid(uuid).ifPresent(token -> {
                log.debug("trySetContext() beset");
                context.setAuthentication(new TokenAuthentication(token));
            });
        }
    }

}
