package mingzuozhibi.coreserver.modules.auth.token;

import mingzuozhibi.coreserver.commons.util.ReturnUtils;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByUuid(String uuid);

    List<Token> findByExpireOnBefore(Instant instant);

    default List<Token> findByExpired() {
        return findByExpireOnBefore(Instant.now());
    }

    default String findByUuid(String uuid, Function<Token, String> function) {
        Optional<Token> byUuid = findByUuid(uuid);
        if (byUuid.isEmpty()) {
            return ReturnUtils.errorMessage("Token不存在");
        }
        return function.apply(byUuid.get());
    }

}
