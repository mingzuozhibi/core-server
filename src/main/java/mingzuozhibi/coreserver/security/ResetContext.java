package mingzuozhibi.coreserver.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class ResetContext {

    private Set<Long> users = Collections.synchronizedSet(new HashSet<>());
    private Set<Long> tokens = Collections.synchronizedSet(new HashSet<>());

    public void resetUserId(Long userId) {
        users.add(userId);
        log.debug("resetUser(userId={})", userId);
    }

    public void resetTokenId(Long tokenId) {
        tokens.add(tokenId);
        log.debug("resetToken(tokenId={})", tokenId);
    }

    public boolean hasUserId(Long userId) {
        return users.remove(userId);
    }

    public boolean hasTokenId(Long tokenId) {
        return tokens.remove(tokenId);
    }

}
