package mingzuozhibi.coreserver.modules.auth.user;

import mingzuozhibi.coreserver.commons.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
public class UserService extends BaseController {

    @Autowired
    private UserRepository userRepository;

    public String findById(Long id, Function<User, String> function) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return errorMessage("用户Id不存在");
        }
        return function.apply(userOpt.get());
    }

    public String findByUsername(String username, Function<User, String> function) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return errorMessage("用户名不存在");
        }
        return function.apply(userOpt.get());
    }

}
