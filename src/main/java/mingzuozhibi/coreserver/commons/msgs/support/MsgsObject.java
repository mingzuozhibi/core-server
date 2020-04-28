package mingzuozhibi.coreserver.commons.msgs.support;

import lombok.Getter;

import java.time.Instant;

import static mingzuozhibi.coreserver.commons.msgs.Msgs.Tag;

@Getter
public class MsgsObject {

    private Tag tag;
    private Level level;
    private String content;
    private Instant createOn;

    public MsgsObject(Tag tag, Level level, String content) {
        this.tag = tag;
        this.level = level;
        this.content = content;
        this.createOn = Instant.now();
    }

    public enum Level {
        DEBUG, INFO, NOTIFY, SUCCESS, WARN, ERROR
    }

}
