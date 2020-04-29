package mingzuozhibi.coreserver.modules.user;

import mingzuozhibi.coreserver.commons.support.ReturnUtils;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.function.Function;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    default String findById(Long id, Function<User, String> function) {
        Optional<User> byId = findById(id);
        if (byId.isEmpty()) {
            return ReturnUtils.errorMessage("用户Id不存在");
        }
        return function.apply(byId.get());
    }

    default String findByUsername(String username, Function<User, String> function) {
        Optional<User> byUsername = findByUsername(username);
        if (byUsername.isEmpty()) {
            return ReturnUtils.errorMessage("用户名不存在");
        }
        return function.apply(byUsername.get());
    }

}
