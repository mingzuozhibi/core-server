package mingzuozhibi.coreserver.modules.auth.token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByUuid(String uuid);

    List<Token> findByExpireOnBefore(Instant instant);

    default List<Token> findByExpired() {
        return findByExpireOnBefore(Instant.now());
    }

}
