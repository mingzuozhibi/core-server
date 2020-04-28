package mingzuozhibi.coreserver.modules.token;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mingzuozhibi.coreserver.commons.base.BaseModel;
import mingzuozhibi.coreserver.modules.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "token")
@Getter
@Setter
@NoArgsConstructor
public class Token extends BaseModel {

    private static final long serialVersionUID = 1L;

    @ManyToOne(optional = false)
    private User user;

    @Column(length = 36, nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false)
    private Instant accessOn;

    @Column(nullable = false)
    private Instant expireOn;

    public Token(User user) {
        this.user = user;
        this.uuid = UUID.randomUUID().toString();
        this.accessOn = Instant.now();
        this.expireOn = Instant.now().plusSeconds(14 * 86400);
    }

    public boolean tokenExpired() {
        return Instant.now().isAfter(expireOn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return uuid.equals(token.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

}
