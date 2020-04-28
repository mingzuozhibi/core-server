package mingzuozhibi.coreserver.modules.token;

import lombok.extern.slf4j.Slf4j;
import mingzuozhibi.coreserver.security.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
public class TokenChecker {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private SessionManager sessionManager;

    @Transactional
    @Scheduled(cron = "0 0/5 * * * ?")
    public void checkToken() {
        List<Token> tokens = tokenRepository.findByExpired();
        if (tokens.size() > 0) {
            tokens.forEach(sessionManager::deleteSession);
            log.info("删除过期Token({}/{})", tokens.size(), tokenRepository.count());
        }
        tokenRepository.deleteAll(tokens);
    }

}
