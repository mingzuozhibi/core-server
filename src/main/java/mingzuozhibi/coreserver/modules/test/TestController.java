package mingzuozhibi.coreserver.modules.test;

import lombok.Data;
import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.commons.logger.JmsLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController extends BaseController {

    @Autowired
    private JmsLogger jmsLogger;

    @Data
    private static class MessageForm {
        private String type;
        private String message;
    }

    @PostMapping("/api/test/info")
    public String info(@RequestBody MessageForm form) {
        jmsLogger.info(form.message);
        return objectResult(true);
    }

}
