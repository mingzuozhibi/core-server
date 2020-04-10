package mingzuozhibi.coreserver.commons.message;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import mingzuozhibi.coreserver.commons.gson.GsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsMessage {

    private static final Gson GSON = GsonUtils.INSTANCE;

    @Autowired
    private JmsTemplate template;

    @Value("${spring.application.name}")
    private String application;

    @Getter
    @AllArgsConstructor
    public static class Message {
        private JmsMessageType type;
        private String moduleName;
        private String content;
    }

    public void doLog(JmsMessageType type, String content) {
        Message message = new Message(type, application, content);
        template.convertAndSend("jms.message", GSON.toJson(message));
    }

}
