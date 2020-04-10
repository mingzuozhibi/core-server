package mingzuozhibi.coreserver.modules.test;

import lombok.Data;
import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.commons.msgs.Msgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController extends BaseController {

    @Autowired
    private Msgs msgs;

    @Data
    private static class MessageForm {
        private String type;
        private String message;
    }

    @PostMapping("/api/test/info")
    public String info(@RequestBody MessageForm form) {
        msgs.info(form.message);
        return objectResult(true);
    }

    @PostMapping("/api/test/debug")
    public String debug(@RequestBody MessageForm form) {
        msgs.debug(form.message);
        return objectResult(true);
    }

}
