package mingzuozhibi.coreserver.modules.auth.token;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mingzuozhibi.coreserver.commons.base.BaseModel;
import mingzuozhibi.coreserver.modules.auth.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity(name = "auth_token")
@Getter
@Setter
@NoArgsConstructor
public class Token extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(optional = false)
    private User user;

    @Column(length = 36, nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false)
    private Instant expireOn;

    public Token(User user) {
        this.user = user;
        this.uuid = UUID.randomUUID().toString();
        this.expireOn = Instant.now().plusSeconds(14 * 86400);
    }

    public boolean tokenExpired() {
        return Instant.now().isAfter(expireOn);
    }

}
