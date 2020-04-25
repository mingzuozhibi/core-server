package mingzuozhibi.coreserver.modules.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mingzuozhibi.coreserver.commons.base.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.Instant;

@Entity(name = "message")
@Getter
@Setter
@NoArgsConstructor
public class Message extends BaseModel {

    @Column(nullable = false, length = 10)
    private String level;

    @Column(nullable = false, length = 20)
    private String module;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private Instant createOn;

    @Column(nullable = false)
    private Instant acceptOn;

}
