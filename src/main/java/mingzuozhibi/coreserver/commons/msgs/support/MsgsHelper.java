package mingzuozhibi.coreserver.commons.msgs.support;

import lombok.extern.slf4j.Slf4j;
import mingzuozhibi.coreserver.commons.msgs.Msgs;
import mingzuozhibi.coreserver.commons.msgs.support.MsgsObject.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static mingzuozhibi.coreserver.commons.gson.GsonHelper.GSON;
import static mingzuozhibi.coreserver.commons.util.Constants.JMS_LOG_KEY;

@Slf4j
@Component
public class MsgsHelper {

    private JmsTemplate jmsTemplate;

    @Autowired
    public MsgsHelper(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public Msgs with(Msgs.Tag tag) {
        return new Msgs(this, tag);
    }

    public void debug(Msgs.Tag tag, String format, Object... args) {
        String content = String.format(format, args);
        log.debug(JMS_LOG_KEY + " => " + content);
        sendMsg(tag, Level.DEBUG, content);
    }

    public void info(Msgs.Tag tag, String format, Object... args) {
        String content = String.format(format, args);
        log.info(JMS_LOG_KEY + " => " + content);
        sendMsg(tag, Level.INFO, content);
    }

    public void notify(Msgs.Tag tag, String format, Object... args) {
        String content = String.format(format, args);
        log.info(JMS_LOG_KEY + " => " + content);
        sendMsg(tag, Level.NOTIFY, content);
    }

    public void success(Msgs.Tag tag, String format, Object... args) {
        String content = String.format(format, args);
        log.info(JMS_LOG_KEY + " => " + content);
        sendMsg(tag, Level.SUCCESS, content);
    }

    public void warn(Msgs.Tag tag, String format, Object... args) {
        String content = String.format(format, args);
        log.warn(JMS_LOG_KEY + " => " + content);
        sendMsg(tag, Level.WARN, content);
    }

    public void error(Msgs.Tag tag, String format, Object... args) {
        String content = String.format(format, args);
        log.error(JMS_LOG_KEY + " => " + content);
        sendMsg(tag, Level.ERROR, content);
    }

    private void sendMsg(Msgs.Tag tag, Level level, String content) {
        MsgsObject msgsObject = new MsgsObject(tag, level, content);
        jmsTemplate.convertAndSend(JMS_LOG_KEY, GSON.toJson(msgsObject));
    }

}
