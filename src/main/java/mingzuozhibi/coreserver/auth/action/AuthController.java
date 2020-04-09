package mingzuozhibi.coreserver.auth.action;

import lombok.Data;
import mingzuozhibi.coreserver.auth.token.Token;
import mingzuozhibi.coreserver.auth.token.TokenRepository;
import mingzuozhibi.coreserver.auth.user.User;
import mingzuozhibi.coreserver.auth.user.UserRepository;
import mingzuozhibi.coreserver.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;

@RestController
public class AuthController extends BaseController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Transactional
    @PostMapping("/auth/token")
    public String authToken(@RequestBody String uuid) {
        // Find Token By Uuid
        Optional<Token> tokenOpt = tokenRepository.findByUuid(uuid);
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
    @DeleteMapping("/auth/token/{id}")
    public String dropToken(@PathVariable Long id) {
        // Find Token By Id
        Optional<Token> tokenOpt = tokenRepository.findById(id);
        if (!tokenOpt.isPresent()) {
            return errorMessage("Token不存在");
        }
        Token token = tokenOpt.get();
        // Drop Token
        tokenRepository.delete(token);
        return objectResult(token);
    }

    @Data
    private static class LoginForm {
        private String username;
        private String password;
    }

    @Transactional
    @PostMapping("/auth/login")
    public String authLogin(@RequestBody LoginForm form) {
        // Find User By Username
        Optional<User> userOpt = userRepository.findByUsername(form.username);
        if (!userOpt.isPresent()) {
            return errorMessage("用户名不存在");
        }
        User user = userOpt.get();
        // Check User Disabled
        if (!user.isEnabled()) {
            return errorMessage("该用户已禁用");
        }
        // Check User Password
        if (!user.getPassword().equals(form.password)) {
            return errorMessage("密码错误");
        }
        // Login Success
        onLoginSuccess(user);
        // Token Created
        return objectResult(createToken(user));
    }

    private Token createToken(User user) {
        return tokenRepository.save(new Token(user));
    }

    private void onLoginSuccess(User user) {
        user.setLoggedOn(Instant.now());
    }

}
