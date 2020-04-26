package mingzuozhibi.coreserver.commons.msgs.support;

import lombok.Getter;
import mingzuozhibi.coreserver.commons.msgs.Index;

import java.time.Instant;

@Getter
public class MsgsObject {

    private Level level;
    private Index index;
    private String content;
    private Instant createOn;

    public MsgsObject(Level level, Index index, String content) {
        this.level = level;
        this.index = index;
        this.content = content;
        this.createOn = Instant.now();
    }

    public enum Level {
        DEBUG, INFO, NOTIFY, SUCCESS, WARN, ERROR
    }

}
