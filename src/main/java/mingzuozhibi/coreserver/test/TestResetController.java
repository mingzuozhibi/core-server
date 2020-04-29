package mingzuozhibi.coreserver.test;

import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.modules.token.TokenRepository;
import mingzuozhibi.coreserver.modules.user.UserRepository;
import mingzuozhibi.coreserver.security.auth.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestResetController extends BaseController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private SessionManager sessionManager;

    @GetMapping("/test/reset/user/{id}")
    public void resetUser(@PathVariable Long id) {
        userRepository.findById(id).ifPresent(user -> {
            sessionManager.deleteSession(user);
        });
    }

    @GetMapping("/test/reset/token/{id}")
    public void resetToken(@PathVariable Long id) {
        tokenRepository.findById(id).ifPresent(token -> {
            sessionManager.deleteSession(token);
        });
    }

}
