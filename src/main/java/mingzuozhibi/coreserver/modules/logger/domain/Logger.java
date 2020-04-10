package mingzuozhibi.coreserver.modules.logger.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mingzuozhibi.coreserver.commons.base.BaseModel;
import mingzuozhibi.coreserver.commons.logger.JmsLogger.Message;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.Instant;

@Entity(name = "logger")
@Getter
@Setter
@NoArgsConstructor
public class Logger extends BaseModel {

    @Column
    private String level;

    @Column
    private String module;

    @Column
    private String content;

    @Column
    private Instant createOn;

    @Column
    private Instant acceptOn;

    public Logger(Message message) {
        this.level = message.getLevel().name();
        this.module = message.getModule();
        this.content = message.getContent();
        this.createOn = message.getCreateOn();
        this.acceptOn = Instant.now();
    }

}
