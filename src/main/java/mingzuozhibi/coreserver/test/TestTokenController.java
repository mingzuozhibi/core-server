package mingzuozhibi.coreserver.test;

import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.modules.token.TokenChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestTokenController extends BaseController {

    @Autowired
    private TokenChecker tokenChecker;

    @GetMapping("/test/token/check")
    public void checkToken() {
        tokenChecker.checkToken();
    }

}
