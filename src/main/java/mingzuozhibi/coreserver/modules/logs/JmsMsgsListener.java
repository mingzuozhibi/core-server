package mingzuozhibi.coreserver.modules.logs;

import com.google.gson.Gson;
import mingzuozhibi.coreserver.commons.gson.GsonUtils;
import mingzuozhibi.coreserver.modules.logs.log.Log;
import mingzuozhibi.coreserver.modules.logs.log.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JmsMsgsListener {

    public static final Gson GSON = GsonUtils.INSTANCE;

    @Autowired
    private LogRepository logRepository;

    @JmsListener(destination = "jms.msgs")
    public void listenJmsMsgs(String json) {
        Log log = GSON.fromJson(json, Log.class);
        log.setAcceptOn(Instant.now());
        logRepository.save(log);
    }

}
