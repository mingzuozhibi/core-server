package mingzuozhibi.coreserver.test;

import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.modules.user.User;
import mingzuozhibi.coreserver.modules.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestUserController extends BaseController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/test/user/setup/test")
    public boolean setupTestUser() {
        if (userRepository.findByUsername("test").isEmpty()) {
            User user = new User("test", "test", true);
            user.getRoles().add("RootAdmin");
            user.getRoles().add("UserAdmin");
            userRepository.save(user);
            return true;
        }
        return false;
    }

}
