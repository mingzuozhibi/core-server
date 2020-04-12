package mingzuozhibi.coreserver.modules.auth.action;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.modules.auth.token.Token;
import mingzuozhibi.coreserver.modules.auth.token.TokenRepository;
import mingzuozhibi.coreserver.modules.auth.user.User;
import mingzuozhibi.coreserver.modules.auth.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
public class AuthController extends BaseController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Data
    private static class RegisterForm {
        private String username;
        private String password;
        private boolean enabled = true;
    }

    @Transactional
    @PostMapping("/api/auth/register")
    public String authRegister(@RequestBody RegisterForm form) {
        // Check User Exists
        if (userRepository.existsByUsername(form.username)) {
            return errorMessage("用户名已存在");
        }
        // Save User
        User user = new User(form.username, form.password, form.enabled);
        userRepository.save(user);
        return objectResult(user);
    }

    @Data
    private static class LoginForm {
        private String username;
        private String password;
    }

    @Transactional
    @PostMapping("/api/auth/login")
    public String authLogin(@RequestBody LoginForm form) {
        // Find User By Username
        Optional<User> userOpt = userRepository.findByUsername(form.username);
        if (userOpt.isEmpty()) {
            return errorMessage("用户名不存在");
        }
        User user = userOpt.get();
        // Check User Disabled
        if (!user.isEnabled()) {
            return errorMessage("该用户已禁用");
        }
        // Check User Password
        if (!Objects.equals(user.getPassword(), form.password)) {
            return errorMessage("密码错误");
        }
        // Login Success
        onLoginSuccess(user);
        // Token Created
        return objectResult(createToken(user));
    }

    @Data
    private static class TokenForm {
        private String uuid;
    }

    @Transactional
    @PostMapping("/api/auth/token")
    public String authToken(@RequestBody TokenForm form) {
        // Find Token By Uuid
        Optional<Token> tokenOpt = tokenRepository.findByUuid(form.uuid);
        if (tokenOpt.isEmpty()) {
            return errorMessage("Token不存在");
        }
        Token token = tokenOpt.get();
        // Check Token Expired
        if (token.tokenExpired()) {
            return errorMessage("Token已失效");
        }
        User user = token.getUser();
        // Check User Disabled
        if (!user.isEnabled()) {
            return errorMessage("该用户已禁用");
        }
        // Login Success
        onLoginSuccess(user);
        return objectResult(token);
    }

    @Transactional
    @DeleteMapping("/api/auth/token")
    public String dropToken(@RequestBody TokenForm form) {
        // Find Token By Uuid
        Optional<Token> tokenOpt = tokenRepository.findByUuid(form.uuid);
        if (tokenOpt.isEmpty()) {
            return errorMessage("Token不存在");
        }
        Token token = tokenOpt.get();
        // Drop Token
        tokenRepository.delete(token);
        return objectResult(token);
    }

    @Transactional
    @GetMapping("/test/checkToken")
    @Scheduled(cron = "0 0/5 * * * ?")
    public void checkToken() {
        List<Token> tokens = tokenRepository.findByExpireOnBefore(Instant.now());
        if (tokens.size() > 0) {
            log.info("删除过期Token({}/{})", tokens.size(), tokenRepository.count());
        }
        tokenRepository.deleteAll(tokens);
    }

    private Token createToken(User user) {
        return tokenRepository.save(new Token(user));
    }

    private void onLoginSuccess(User user) {
        user.setLoggedOn(Instant.now());
    }

}
