package mingzuozhibi.coreserver.modules.logger;

import com.google.gson.Gson;
import mingzuozhibi.coreserver.commons.gson.GsonUtils;
import mingzuozhibi.coreserver.commons.logger.JmsLogger.Message;
import mingzuozhibi.coreserver.commons.time.Formatter;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;

@Component
public class JmsLoggerListener {

    public static final Gson GSON = GsonUtils.INSTANCE;

    @JmsListener(destination = "jms.logger")
    public void listenJmsLogger(String json) {
        Message message = GSON.fromJson(json, Message.class);
        System.out.println("Level: " + message.getLevel());
        System.out.println("Module: " + message.getModule());
        System.out.println("Content: " + message.getContent());
        System.out.println("CreateOn: " + formatInstant(message.getCreateOn()));
        System.out.println("AcceptOn: " + formatInstant(Instant.now()));
        System.out.println();
    }

    private String formatInstant(Instant instant) {
        return instant.atZone(ZoneId.systemDefault()).format(Formatter.DATE_TIME_FORMATTER);
    }

}
