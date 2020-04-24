package mingzuozhibi.coreserver.test;

import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.modules.auth.token.TokenChecker;
import mingzuozhibi.coreserver.security.ResetContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestResetController extends BaseController {

    @Autowired
    private ResetContext resetContext;

    @Autowired
    private TokenChecker tokenChecker;

    @GetMapping("/test/resetUser/{id}")
    public void testResetUser(@PathVariable Long id) {
        resetContext.resetUserId(id);
    }

    @GetMapping("/test/resetToken/{id}")
    public void testResetToken(@PathVariable Long id) {
        resetContext.resetTokenId(id);
    }

    @GetMapping("/test/checkToken")
    public void testCheckToken() {
        tokenChecker.checkToken();
    }

}
