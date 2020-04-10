package mingzuozhibi.coreserver.commons.msgs;

import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import mingzuozhibi.coreserver.commons.gson.GsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
public class Msgs {

    private static final Gson GSON = GsonUtils.INSTANCE;

    @Value("${spring.application.name}")
    private String module;

    @Autowired
    private JmsTemplate template;

    private void sendMsg(Type type, String content) {
        Message message = new Message(type, module, content);
        template.convertAndSend("jms.msgs", GSON.toJson(message));
    }

    public void debug(String format, Object... args) {
        String content = String.format(format, args);
        sendMsg(Type.DEBUG, content);
        log.debug("jms.msgs => " + content);
    }

    public void info(String format, Object... args) {
        String content = String.format(format, args);
        sendMsg(Type.INFO, content);
        log.info("jms.msgs => " + content);
    }

    public void notify(String format, Object... args) {
        String content = String.format(format, args);
        sendMsg(Type.NOTIFY, content);
        log.info("jms.msgs => " + content);
    }

    public void success(String format, Object... args) {
        String content = String.format(format, args);
        sendMsg(Type.SUCCESS, content);
        log.info("jms.msgs => " + content);
    }

    public void warn(String format, Object... args) {
        String content = String.format(format, args);
        sendMsg(Type.WARN, content);
        log.warn("jms.msgs => " + content);
    }

    public void error(String format, Object... args) {
        String content = String.format(format, args);
        sendMsg(Type.ERROR, content);
        log.error("jms.msgs => " + content);
    }

    public enum Type {
        DEBUG, INFO, NOTIFY, SUCCESS, WARN, ERROR
    }

    @Data
    public static class Message {

        private Type level;
        private String module;
        private String content;
        private Instant createOn;

        public Message(Type level, String module, String content) {
            this.level = level;
            this.module = module;
            this.content = content;
            this.createOn = Instant.now();
        }

    }

}
