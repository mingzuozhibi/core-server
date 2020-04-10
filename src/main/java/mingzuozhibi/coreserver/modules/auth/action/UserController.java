package mingzuozhibi.coreserver.modules.auth.action;

import lombok.Data;
import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.modules.auth.user.User;
import mingzuozhibi.coreserver.modules.auth.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class UserController extends BaseController {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @GetMapping("/api/users")
    @PreAuthorize("hasRole('UserAdmin')")
    public String findAll(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "20") int pageSize) {
        // Find User Of Page
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<User> userPage = userRepository.findAll(pageRequest);
        List<User> userList = userPage.getContent();
        return objectResult(userList, userPage);
    }

    @Transactional
    @GetMapping("/api/users/{id}")
    @PreAuthorize("hasRole('UserAdmin')")
    public String findById(@PathVariable Long id) {
        // Find User By Id
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return errorMessage("用户Id不存在");
        }
        User user = userOpt.get();
        return objectResult(user);
    }

    @Transactional
    @GetMapping("/api/users/find/username/{username}")
    @PreAuthorize("hasRole('UserAdmin')")
    public String findByUsername(@PathVariable String username) {
        // Find User By Username
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return errorMessage("用户名不存在");
        }
        User user = userOpt.get();
        return objectResult(user);
    }

    @Data
    private static class SetForm {
        private boolean enabled;
        private Set<String> roles = new HashSet<>();
    }

    @Transactional
    @PutMapping("/api/users/{id}")
    @PreAuthorize("hasRole('UserAdmin')")
    public String setOne(@PathVariable Long id, @RequestBody SetForm form) {
        // Find User By Id
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return errorMessage("用户Id不存在");
        }
        User user = userOpt.get();
        // Edit User
        user.setRoles(form.roles);
        user.setEnabled(form.enabled);
        return objectResult(user);
    }

}
