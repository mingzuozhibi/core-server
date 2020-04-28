package mingzuozhibi.coreserver.commons.msgs;

import mingzuozhibi.coreserver.commons.msgs.support.MsgsHelper;
import org.springframework.beans.factory.annotation.Autowired;

public class Msgs {

    public enum Tag {
        Default, Test, User
    }

    public Msgs with(Tag tag) {
        return new Msgs(msgsHelper, tag);
    }

    private MsgsHelper msgsHelper;
    private Tag tag;

    @Autowired
    public Msgs(MsgsHelper msgsHelper, Tag tag) {
        this.msgsHelper = msgsHelper;
        this.tag = tag;
    }

    public void debug(String format, Object... args) {
        msgsHelper.debug(tag, format, args);
    }

    public void info(String format, Object... args) {
        msgsHelper.info(tag, format, args);
    }

    public void notify(String format, Object... args) {
        msgsHelper.notify(tag, format, args);
    }

    public void success(String format, Object... args) {
        msgsHelper.success(tag, format, args);
    }

    public void warn(String format, Object... args) {
        msgsHelper.warn(tag, format, args);
    }

    public void error(String format, Object... args) {
        msgsHelper.error(tag, format, args);
    }

}
