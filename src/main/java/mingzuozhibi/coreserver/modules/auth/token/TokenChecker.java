package mingzuozhibi.coreserver.modules.auth.token;

import lombok.extern.slf4j.Slf4j;
import mingzuozhibi.coreserver.security.ResetContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Component
public class TokenChecker {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private ResetContext resetContext;

    @Transactional
    @Scheduled(cron = "0 0/5 * * * ?")
    public void checkToken() {
        List<Token> tokens = tokenRepository.findByExpireOnBefore(Instant.now());
        if (tokens.size() > 0) {
            tokens.forEach(token -> {
                resetContext.resetTokenId(token.getId());
            });
            log.info("删除过期Token({}/{})", tokens.size(), tokenRepository.count());
        }
        tokenRepository.deleteAll(tokens);
    }

}
