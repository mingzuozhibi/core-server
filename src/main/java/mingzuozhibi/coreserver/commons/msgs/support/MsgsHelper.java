package mingzuozhibi.coreserver.commons.msgs.support;

import lombok.extern.slf4j.Slf4j;
import mingzuozhibi.coreserver.commons.msgs.Index;
import mingzuozhibi.coreserver.commons.msgs.Msgs;
import mingzuozhibi.coreserver.commons.msgs.support.MsgsObject.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static mingzuozhibi.coreserver.commons.gson.GsonHelper.GSON;
import static mingzuozhibi.coreserver.commons.util.JmsKeys.JMS_LOG_KEY;

@Slf4j
@Component
public class MsgsHelper {

    private JmsTemplate jmsTemplate;

    @Autowired
    public MsgsHelper(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public Msgs with(Index index) {
        return new Msgs(this, index);
    }

    public void debug(Index index, String format, Object... args) {
        String content = String.format(format, args);
        log.debug(JMS_LOG_KEY + " => " + content);
        sendMsg(Level.DEBUG, index, content);
    }

    public void info(Index index, String format, Object... args) {
        String content = String.format(format, args);
        log.info(JMS_LOG_KEY + " => " + content);
        sendMsg(Level.INFO, index, content);
    }

    public void notify(Index index, String format, Object... args) {
        String content = String.format(format, args);
        log.info(JMS_LOG_KEY + " => " + content);
        sendMsg(Level.NOTIFY, index, content);
    }

    public void success(Index index, String format, Object... args) {
        String content = String.format(format, args);
        log.info(JMS_LOG_KEY + " => " + content);
        sendMsg(Level.SUCCESS, index, content);
    }

    public void warn(Index index, String format, Object... args) {
        String content = String.format(format, args);
        log.warn(JMS_LOG_KEY + " => " + content);
        sendMsg(Level.WARN, index, content);
    }

    public void error(Index index, String format, Object... args) {
        String content = String.format(format, args);
        log.error(JMS_LOG_KEY + " => " + content);
        sendMsg(Level.ERROR, index, content);
    }

    private void sendMsg(Level level, Index index, String content) {
        MsgsObject msgsObject = new MsgsObject(level, index, content);
        jmsTemplate.convertAndSend(JMS_LOG_KEY, GSON.toJson(msgsObject));
    }

}
