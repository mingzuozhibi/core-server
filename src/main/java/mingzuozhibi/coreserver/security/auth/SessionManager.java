package mingzuozhibi.coreserver.security.auth;

import lombok.extern.slf4j.Slf4j;
import mingzuozhibi.coreserver.modules.token.Token;
import mingzuozhibi.coreserver.modules.user.User;
import mingzuozhibi.coreserver.security.TokenAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SessionManager {

    @Autowired
    private FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    public void deleteSession(User user) {
        log.debug("deleteSession(user={})", user.getUsername());
        sessionRepository.findByPrincipalName(user.getUsername()).forEach((id, session) -> {
            log.info("deleted: user={}, session={}", user.getUsername(), id);
            sessionRepository.deleteById(id);
        });
    }

    public void deleteSession(Token token) {
        log.debug("deleteSession(user={}, token={})", token.getUser().getUsername(), token.getId());
        sessionRepository.findByPrincipalName(token.getUser().getUsername()).forEach((id, session) -> {
            SecurityContextImpl securityContext = session.getAttribute("SPRING_SECURITY_CONTEXT");
            Authentication authentication = securityContext.getAuthentication();
            if (authentication instanceof TokenAuthentication) {
                if (((TokenAuthentication) authentication).getToken().equals(token)) {
                    log.info("deleted: user={}, session={}", token.getUser().getUsername(), id);
                    sessionRepository.deleteById(id);
                }
            }
        });
    }

    public void updateSession(Token token) {
        SecurityContextHolder.getContext().setAuthentication(new TokenAuthentication(token));
    }

}
