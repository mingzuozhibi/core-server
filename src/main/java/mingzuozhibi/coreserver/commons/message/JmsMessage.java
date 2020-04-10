package mingzuozhibi.coreserver.commons.message;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mingzuozhibi.coreserver.commons.gson.GsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JmsMessage {

    private static final Gson GSON = GsonUtils.INSTANCE;

    @Value("${spring.application.name}")
    private String moduleName;

    @Autowired
    private JmsTemplate template;

    @Getter
    @AllArgsConstructor
    public static class Message {
        private JmsMessageType type;
        private String moduleName;
        private String content;
    }

    public void sendMsg(JmsMessageType type, String content) {
        Message message = new Message(type, moduleName, content);
        template.convertAndSend("jms.message", GSON.toJson(message));
    }

    public void debug(String format, Object... args) {
        String content = String.format(format, args);
        sendMsg(JmsMessageType.DEBUG, content);
        log.debug("jms.message => " + content);
    }

    public void info(String format, Object... args) {
        String content = String.format(format, args);
        sendMsg(JmsMessageType.INFO, content);
        log.info("jms.message => " + content);
    }

    public void notify(String format, Object... args) {
        String content = String.format(format, args);
        sendMsg(JmsMessageType.NOTIFY, content);
        log.info("jms.message => " + content);
    }

    public void success(String format, Object... args) {
        String content = String.format(format, args);
        sendMsg(JmsMessageType.SUCCESS, content);
        log.info("jms.message => " + content);
    }

    public void warn(String format, Object... args) {
        String content = String.format(format, args);
        sendMsg(JmsMessageType.WARN, content);
        log.warn("jms.message => " + content);
    }

    public void error(String format, Object... args) {
        String content = String.format(format, args);
        sendMsg(JmsMessageType.ERROR, content);
        log.error("jms.message => " + content);
    }

}
