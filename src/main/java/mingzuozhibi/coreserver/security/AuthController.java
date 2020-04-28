package mingzuozhibi.coreserver.security;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.modules.token.Token;
import mingzuozhibi.coreserver.modules.token.TokenRepository;
import mingzuozhibi.coreserver.modules.user.User;
import mingzuozhibi.coreserver.modules.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Objects;

@Slf4j
@RestController
public class AuthController extends BaseController {

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Data
    private static class UserForm {
        private String username;
        private String password;
        private boolean enabled = true;
    }

    @Transactional
    @PostMapping("/api/auth/register")
    public String authRegister(@RequestBody UserForm form) {
        if (userRepository.existsByUsername(form.username)) {
            return errorMessage("用户名已存在");
        }
        User user = new User(form.username, form.password, form.enabled);
        userRepository.save(user);
        return objectResult(user);
    }

    @Transactional
    @PostMapping("/api/auth/login")
    public String authLogin(@RequestBody UserForm form) {
        return userRepository.findByUsername(form.username, user -> {
            if (!user.isEnabled()) {
                return errorMessage("该用户已禁用");
            }
            if (!Objects.equals(form.password, user.getPassword())) {
                return errorMessage("密码错误");
            }
            Token token = createToken(user);
            onLoginSuccess(user, token);
            return objectResult(token);
        });
    }

    @Data
    private static class TokenForm {
        private String uuid;
    }

    @Transactional
    @PostMapping("/api/auth/token")
    public String authToken(@RequestBody TokenForm form) {
        return tokenRepository.findByUuid(form.uuid, token -> {
            if (token.tokenExpired()) {
                return errorMessage("Token已失效");
            }
            User user = token.getUser();
            if (!user.isEnabled()) {
                return errorMessage("该用户已禁用");
            }
            onLoginSuccess(user, token);
            return objectResult(token);
        });
    }

    @Transactional
    @DeleteMapping("/api/auth/token")
    public String dropToken(@RequestBody TokenForm form) {
        return tokenRepository.findByUuid(form.uuid, token -> {
            tokenRepository.delete(token);
            sessionManager.deleteSession(token);
            return objectResult(token);
        });
    }

    private Token createToken(User user) {
        return tokenRepository.save(new Token(user));
    }

    private void onLoginSuccess(User user, Token token) {
        user.setLoggedOn(Instant.now());
        token.setAccessOn(Instant.now());
        Instant afterSevenDays = Instant.now().plusSeconds(7 * 86400);
        if (token.getExpireOn().isBefore(afterSevenDays)) {
            token.setExpireOn(afterSevenDays);
        }
        sessionManager.updateSession(token);
    }

}
