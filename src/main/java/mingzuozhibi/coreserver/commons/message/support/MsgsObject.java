package mingzuozhibi.coreserver.commons.message.support;

import lombok.Getter;
import mingzuozhibi.coreserver.commons.message.enums.Level;
import mingzuozhibi.coreserver.commons.message.enums.Tag;

import java.time.Instant;

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

}
