package mingzuozhibi.coreserver.commons.message.support;

import lombok.Getter;
import mingzuozhibi.coreserver.commons.message.enums.Index;
import mingzuozhibi.coreserver.commons.message.enums.Level;

import java.time.Instant;

@Getter
public class MsgsObject {

    private Index index;
    private Level level;
    private String content;
    private Instant createOn;

    public MsgsObject(Index index, Level level, String content) {
        this.index = index;
        this.level = level;
        this.content = content;
        this.createOn = Instant.now();
    }

}
