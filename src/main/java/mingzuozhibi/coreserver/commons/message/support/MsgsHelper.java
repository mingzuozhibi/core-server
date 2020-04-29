package mingzuozhibi.coreserver.commons.message.support;

import lombok.extern.slf4j.Slf4j;
import mingzuozhibi.coreserver.commons.message.Msgs;
import mingzuozhibi.coreserver.commons.message.enums.Index;
import mingzuozhibi.coreserver.commons.message.enums.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static mingzuozhibi.coreserver.commons.support.QueueKeys.JMS_LOG_KEY;
import static mingzuozhibi.coreserver.commons.support.gson.GsonHelper.GSON;

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
        sendMsg(index, Level.DEBUG, content);
    }

    public void info(Index index, String format, Object... args) {
        String content = String.format(format, args);
        log.info(JMS_LOG_KEY + " => " + content);
        sendMsg(index, Level.INFO, content);
    }

    public void notify(Index index, String format, Object... args) {
        String content = String.format(format, args);
        log.info(JMS_LOG_KEY + " => " + content);
        sendMsg(index, Level.NOTIFY, content);
    }

    public void success(Index index, String format, Object... args) {
        String content = String.format(format, args);
        log.info(JMS_LOG_KEY + " => " + content);
        sendMsg(index, Level.SUCCESS, content);
    }

    public void warn(Index index, String format, Object... args) {
        String content = String.format(format, args);
        log.warn(JMS_LOG_KEY + " => " + content);
        sendMsg(index, Level.WARN, content);
    }

    public void error(Index index, String format, Object... args) {
        String content = String.format(format, args);
        log.error(JMS_LOG_KEY + " => " + content);
        sendMsg(index, Level.ERROR, content);
    }

    public void sendMsg(Index index, Level level, String content) {
        MsgsObject msgsObject = new MsgsObject(index, level, content);
        jmsTemplate.convertAndSend(JMS_LOG_KEY, GSON.toJson(msgsObject));
    }

}
