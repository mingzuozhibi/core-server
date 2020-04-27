package mingzuozhibi.coreserver.modules.user;

import lombok.Data;
import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.commons.msgs.Index;
import mingzuozhibi.coreserver.commons.msgs.Msgs;
import mingzuozhibi.coreserver.commons.msgs.MsgsWired;
import mingzuozhibi.coreserver.commons.util.SecurityUtils;
import mingzuozhibi.coreserver.security.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;

import static mingzuozhibi.coreserver.commons.util.SecurityUtils.getCurrentUsername;
import static mingzuozhibi.coreserver.modules.user.User.ALL_ROLES;

@RestController
public class UserController extends BaseController {

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private UserRepository userRepository;

    @MsgsWired(Index.User)
    private Msgs msgs;

    @Transactional
    @GetMapping("/api/users")
    @PreAuthorize("hasRole('UserAdmin')")
    public String findAll(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "20") int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        return objectResult(userRepository.findAll(pageRequest));
    }

    @Transactional
    @GetMapping("/api/users/{id}")
    @PreAuthorize("hasRole('UserAdmin')")
    public String findById(@PathVariable Long id) {
        return userRepository.findById(id, this::objectResult);
    }

    @Transactional
    @GetMapping("/api/users/find/username/{username}")
    @PreAuthorize("hasRole('UserAdmin')")
    public String findByUsername(@PathVariable String username) {
        return userRepository.findByUsername(username, this::objectResult);
    }

    @Data
    private static class EditForm {
        private boolean enabled;
        private String changedRole;
    }

    @Transactional
    @PutMapping("/api/users/{id}/enabled")
    @PreAuthorize("hasRole('UserAdmin')")
    public String setEnabled(@PathVariable Long id, @RequestBody EditForm form) {
        return userRepository.findById(id, user -> {
            user.getRoles().stream()
                .filter(Set.of("RootAdmin", "UserAdmin")::contains)
                .forEach(this::checkSecurityOfRole);
            if (user.isEnabled() != form.enabled) {
                user.setEnabled(form.enabled);
                sessionManager.deleteSession(user);
                msgs.info("用户%s设置%s的启用状态为%b", getCurrentUsername(), user.getUsername(), form.enabled);
            }
            return objectResult(user);
        });
    }

    @Transactional
    @PostMapping("/api/users/{id}/roles")
    @PreAuthorize("hasRole('UserAdmin')")
    public String pushRole(@PathVariable Long id, @RequestBody EditForm form) {
        return userRepository.findById(id, user -> {
            checkSecurityOfRole(form.changedRole);
            if (user.getRoles().add(form.changedRole)) {
                sessionManager.deleteSession(user);
                msgs.info("用户%s给%s添加了权限%s", getCurrentUsername(), user.getUsername(), form.changedRole);
            }
            return objectResult(user);
        });
    }

    @Transactional
    @DeleteMapping("/api/users/{id}/roles")
    @PreAuthorize("hasRole('UserAdmin')")
    public String dropRole(@PathVariable Long id, @RequestBody EditForm form) {
        return userRepository.findById(id, user -> {
            checkSecurityOfRole(form.changedRole);
            if (user.getRoles().remove(form.changedRole)) {
                sessionManager.deleteSession(user);
                msgs.info("用户%s从%s移除了权限%s", getCurrentUsername(), user.getUsername(), form.changedRole);
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
