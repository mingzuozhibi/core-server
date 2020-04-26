package mingzuozhibi.coreserver.security;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import mingzuozhibi.coreserver.modules.auth.token.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
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
        if (!Strings.isNullOrEmpty(uuid)) {
            tokenRepository.findByUuid(uuid).ifPresent(token -> {
                context.setAuthentication(new TokenAuthentication(token));
            });
        }
    }

}
