package mingzuozhibi.coreserver.security.support;

import mingzuozhibi.coreserver.modules.token.Token;
import mingzuozhibi.coreserver.modules.user.User;
import mingzuozhibi.coreserver.security.TokenAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.Set;

public abstract class SecurityUtils {

    public interface SecurityCheckFunction {
        boolean apply(Set<String> roles);
    }

    public static void doSecurityCheck(SecurityCheckFunction function) throws SecurityException {
        User user = getCurrentUser().orElseThrow(() -> new SecurityException("你似乎并未正确认证"));
        if (!function.apply(user.getRoles())) {
            throw new SecurityException("权限不足以进行此操作");
        }
    }

    public static Optional<TokenAuthentication> getTokenAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof TokenAuthentication) {
            return Optional.of((TokenAuthentication) authentication);
        }
        return Optional.empty();
    }

    public static Optional<Token> getCurrentToken() {
        return getTokenAuthentication().map(TokenAuthentication::getToken);
    }

    public static Optional<User> getCurrentUser() {
        return getCurrentToken().map(Token::getUser);
    }

    public static String getCurrentUsername() {
        return getCurrentUser().map(User::getUsername).orElse("Unknown");
    }

}
