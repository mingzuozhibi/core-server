package mingzuozhibi.coreserver.modules.user;

import lombok.Data;
import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.commons.message.Msgs;
import mingzuozhibi.coreserver.commons.message.MsgsWired;
import mingzuozhibi.coreserver.commons.message.enums.Index;
import mingzuozhibi.coreserver.commons.support.page.PageDefault;
import mingzuozhibi.coreserver.commons.support.page.PageParams;
import mingzuozhibi.coreserver.modules.user.enums.Role;
import mingzuozhibi.coreserver.security.auth.SessionManager;
import mingzuozhibi.coreserver.security.support.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@PreAuthorize("hasAnyRole('RootAdmin','UserAdmin')")
public class UserController extends BaseController {

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private UserRepository userRepository;

    @MsgsWired(Index.User)
    private Msgs msgs;

    @GetMapping("/api/users")
    @Transactional(readOnly = true)
    public String findAll(@PageDefault(size = 20) PageParams params) {
        return objectResult(userRepository.findAll(params.toPageable()));
    }

    @GetMapping("/api/users/{id}")
    @Transactional(readOnly = true)
    public String findById(@PathVariable Long id) {
        return userRepository.findById(id, this::objectResult);
    }

    @GetMapping("/api/users/find/username/{username}")
    @Transactional(readOnly = true)
    public String findByUsername(@PathVariable String username) {
        return userRepository.findByUsername(username, this::objectResult);
    }

    @Data
    private static class EditForm {
        private boolean enabled;
        private Role targetRole;
    }

    @Transactional
    @PutMapping("/api/users/{id}/enabled")
    public String setEnabled(@PathVariable Long id, @RequestBody EditForm form) {
        return userRepository.findById(id, user -> {
            checkRootAdminUser(user);
            if (user.isRootAdmin()) {
                throw new SecurityException("不能禁用RootAdmin用户");
            }
            if (user.isEnabled() != form.enabled) {
                user.setEnabled(form.enabled);
                sessionManager.deleteSession(user);
                msgs.info("用户%s设置%s的启用状态为%b",
                    SecurityUtils.getCurrentUsername(), user.getUsername(), form.enabled);
            }
            return objectResult(user);
        });
    }

    @Transactional
    @PostMapping("/api/users/{id}/roles")
    public String pushRole(@PathVariable Long id, @RequestBody EditForm form) {
        return userRepository.findById(id, user -> {
            checkRootAdminUser(user);
            checkUserAndRole(user, form.targetRole);
            if (user.getRoles().add(form.targetRole)) {
                sessionManager.deleteSession(user);
                msgs.info("用户%s给%s添加了权限%s",
                    SecurityUtils.getCurrentUsername(), user.getUsername(), form.targetRole);
            }
            return objectResult(user);
        });
    }

    @Transactional
    @DeleteMapping("/api/users/{id}/roles")
    public String dropRole(@PathVariable Long id, @RequestBody EditForm form) {
        return userRepository.findById(id, user -> {
            checkRootAdminUser(user);
            checkUserAndRole(user, form.targetRole);
            if (user.getRoles().remove(form.targetRole)) {
                sessionManager.deleteSession(user);
                msgs.info("用户%s从%s移除了权限%s",
                    SecurityUtils.getCurrentUsername(), user.getUsername(), form.targetRole);
            }
            return objectResult(user);
        });
    }

    private void checkRootAdminUser(User targetUser) {
        if (!isSameUser(targetUser) && targetUser.isRootAdmin()) {
            throw new SecurityException("不能变更RootAdmin用户");
        }
    }

    private void checkUserAndRole(User targetUser, Role targetRole) {
        if (targetRole == Role.RootAdmin) {
            throw new SecurityException("不能变更RootAdmin权限");
        }
        if (isSameUser(targetUser) && targetRole == Role.UserAdmin) {
            throw new SecurityException("不能删除自己的UserAdmin权限");
        }
    }

    private boolean isSameUser(User targetUser) {
        User currentUser = SecurityUtils.getUserOrThrow();
        return Objects.equals(currentUser, targetUser);
    }

}
