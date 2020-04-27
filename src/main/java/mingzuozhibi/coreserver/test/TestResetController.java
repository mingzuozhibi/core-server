package mingzuozhibi.coreserver.test;

import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.modules.token.TokenChecker;
import mingzuozhibi.coreserver.modules.token.TokenRepository;
import mingzuozhibi.coreserver.modules.user.UserRepository;
import mingzuozhibi.coreserver.security.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestResetController extends BaseController {

    @Autowired
    private TokenChecker tokenChecker;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private SessionManager sessionManager;

    @GetMapping("/test/resetUser/{id}")
    public void testResetUser(@PathVariable Long id) {
        userRepository.findById(id).ifPresent(user -> {
            sessionManager.deleteSession(user);
        });
    }

    @GetMapping("/test/resetToken/{id}")
    public void testResetToken(@PathVariable Long id) {
        tokenRepository.findById(id).ifPresent(token -> {
            sessionManager.deleteSession(token);
        });
    }

    @GetMapping("/test/checkToken")
    public void testCheckToken() {
        tokenChecker.checkToken();
    }

}
