package mingzuozhibi.coreserver.commons.message;

import mingzuozhibi.coreserver.commons.message.enums.Index;
import mingzuozhibi.coreserver.commons.message.support.MsgsHelper;
import org.springframework.beans.factory.annotation.Autowired;

public class Msgs {

    public Msgs with(Index index) {
        return new Msgs(msgsHelper, index);
    }

    private MsgsHelper msgsHelper;
    private Index index;

    @Autowired
    public Msgs(MsgsHelper msgsHelper, Index index) {
        this.msgsHelper = msgsHelper;
        this.index = index;
    }

    public void debug(String format, Object... args) {
        msgsHelper.debug(index, format, args);
    }

    public void info(String format, Object... args) {
        msgsHelper.info(index, format, args);
    }

    public void notify(String format, Object... args) {
        msgsHelper.notify(index, format, args);
    }

    public void success(String format, Object... args) {
        msgsHelper.success(index, format, args);
    }

    public void warn(String format, Object... args) {
        msgsHelper.warn(index, format, args);
    }

    public void error(String format, Object... args) {
        msgsHelper.error(index, format, args);
    }

}
