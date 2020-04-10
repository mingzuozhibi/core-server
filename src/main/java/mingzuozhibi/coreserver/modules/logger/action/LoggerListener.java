package mingzuozhibi.coreserver.modules.logger.action;

import com.google.gson.Gson;
import mingzuozhibi.coreserver.commons.gson.GsonUtils;
import mingzuozhibi.coreserver.commons.logger.JmsLogger.Message;
import mingzuozhibi.coreserver.modules.logger.domain.Logger;
import mingzuozhibi.coreserver.modules.logger.domain.LoggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class LoggerListener {

    public static final Gson GSON = GsonUtils.INSTANCE;

    @Autowired
    private LoggerRepository loggerRepository;

    @JmsListener(destination = "jms.logger")
    public void listenJmsLogger(String json) {
        loggerRepository.save(new Logger(GSON.fromJson(json, Message.class)));
    }

}
