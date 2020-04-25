package mingzuozhibi.coreserver.commons.msgs;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static mingzuozhibi.coreserver.commons.gson.GsonHelper.GSON;

@Slf4j
@Component
public class Msgs {

    @Value("${spring.application.name}")
    private String module;

    @Autowired
    private JmsTemplate template;

    private void sendMsg(Level level, String content) {
        Message message = new Message(level, module, content);
        template.convertAndSend("module.message", GSON.toJson(message));
    }

    public void debug(String format, Object... args) {
        String content = String.format(format, args);
        sendMsg(Level.DEBUG, content);
        log.debug("module.message => " + content);
    }

    public void info(String format, Object... args) {
        String content = String.format(format, args);
        sendMsg(Level.INFO, content);
        log.info("module.message => " + content);
    }

    public void notify(String format, Object... args) {
        String content = String.format(format, args);
        sendMsg(Level.NOTIFY, content);
        log.info("module.message => " + content);
    }

    public void success(String format, Object... args) {
        String content = String.format(format, args);
        sendMsg(Level.SUCCESS, content);
        log.info("module.message => " + content);
    }

    public void warn(String format, Object... args) {
        String content = String.format(format, args);
        sendMsg(Level.WARN, content);
        log.warn("module.message => " + content);
    }

    public void error(String format, Object... args) {
        String content = String.format(format, args);
        sendMsg(Level.ERROR, content);
        log.error("module.message => " + content);
    }

    public enum Level {
        DEBUG, INFO, NOTIFY, SUCCESS, WARN, ERROR
    }

    @Data
    public static class Message {

        private Level level;
        private String module;
        private String content;
        private Instant createOn;

        public Message(Level level, String module, String content) {
            this.level = level;
            this.module = module;
            this.content = content;
            this.createOn = Instant.now();
        }

    }

}
