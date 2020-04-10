package mingzuozhibi.coreserver.modules.logger.action;

import com.google.gson.Gson;
import mingzuozhibi.coreserver.commons.gson.GsonUtils;
import mingzuozhibi.coreserver.modules.logger.domain.Logger;
import mingzuozhibi.coreserver.modules.logger.domain.LoggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class LoggerListener {

    public static final Gson GSON = GsonUtils.INSTANCE;

    @Autowired
    private LoggerRepository loggerRepository;

    @JmsListener(destination = "jms.msgs")
    public void listenJmsMsgs(String json) {
        Logger logger = GSON.fromJson(json, Logger.class);
        logger.setAcceptOn(Instant.now());
        loggerRepository.save(logger);
    }

}
