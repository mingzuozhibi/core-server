package mingzuozhibi.coreserver.modules.auth.user;

import lombok.Data;
import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.commons.msgs.Msgs;
import mingzuozhibi.coreserver.commons.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static mingzuozhibi.coreserver.commons.util.SecurityUtils.getCurrentUsername;
import static mingzuozhibi.coreserver.modules.auth.user.User.ALL_ROLES;

@RestController
public class UserController extends BaseController {

    @Autowired
    private Msgs msgs;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @GetMapping("/api/users")
    @PreAuthorize("hasRole('UserAdmin')")
    public String findAll(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "20") int pageSize) {
        // Find User Of Page
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        return objectResult(userRepository.findAll(pageRequest));
    }

    @Transactional
    @GetMapping("/api/users/{id}")
    @PreAuthorize("hasRole('UserAdmin')")
    public String findById(@PathVariable Long id) {
        return userService.findById(id, this::objectResult);
    }

    @Transactional
    @GetMapping("/api/users/find/username/{username}")
    @PreAuthorize("hasRole('UserAdmin')")
    public String findByUsername(@PathVariable String username) {
        return userService.findByUsername(username, this::objectResult);
    }

    @Data
    private static class SetForm {
        private boolean enabled;
    }

    @Transactional
    @PutMapping("/api/users/{id}")
    @PreAuthorize("hasRole('UserAdmin')")
    public String setOne(@PathVariable Long id, @RequestBody SetForm form) {
        return userService.findById(id, user -> {
            user.getRoles().forEach(this::checkSecurityOfRole);
            if (user.isEnabled() != form.enabled) {
                user.setEnabled(form.enabled);
                msgs.info("用户%s设置%s的启用状态为%b", getCurrentUsername(), user.getUsername(), form.enabled);
            }
            return objectResult(user);
        });
    }

    @Data
    private static class RoleForm {
        private String role;
    }

    @Transactional
    @PostMapping("/api/users/{id}/roles")
    @PreAuthorize("hasRole('UserAdmin')")
    public String pushRole(@PathVariable Long id, @RequestBody RoleForm form) {
        return userService.findById(id, user -> {
            checkSecurityOfRole(form.role);
            if (user.getRoles().add(form.role)) {
                msgs.info("用户%s给%s添加了权限%s", getCurrentUsername(), user.getUsername(), form.role);
            }
            return objectResult(user);
        });
    }

    @Transactional
    @DeleteMapping("/api/users/{id}/roles")
    @PreAuthorize("hasRole('UserAdmin')")
    public String dropRole(@PathVariable Long id, @RequestBody RoleForm form) {
        return userService.findById(id, user -> {
            checkSecurityOfRole(form.role);
            if (user.getRoles().remove(form.role)) {
                msgs.info("用户%s从%s移除了权限%s", getCurrentUsername(), user.getUsername(), form.role);
            }
            return objectResult(user);
        });
    }

    private void checkSecurityOfRole(String role) {
        Objects.requireNonNull(role);
        SecurityUtils.doSecurityCheck(roles -> {
            if (role.equals("RootAdmin")) {
                return false;
            }
            if (role.equals("UserAdmin") && !roles.contains("RootAdmin")) {
                return false;
            }
            if (!ALL_ROLES.contains(role)) {
                return false;
            }
            return true;
        });
    }

}
