package mingzuozhibi.coreserver.modules.test;

import lombok.Data;
import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.commons.message.JmsMessage;
import mingzuozhibi.coreserver.commons.message.JmsMessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController extends BaseController {

    @Autowired
    private JmsMessage jmsMessage;

    @Data
    private static class MessageForm {
        private String type;
        private String message;
    }

    @PostMapping("/api/hello")
    public String hello(@RequestBody MessageForm form) {
        jmsMessage.doLog(JmsMessageType.valueOf(form.type), form.message);
        return objectResult(true);
    }

}
