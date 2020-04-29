package mingzuozhibi.coreserver.modules.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mingzuozhibi.coreserver.commons.base.BaseModel;
import mingzuozhibi.coreserver.commons.support.gson.GsonIgnore;
import mingzuozhibi.coreserver.modules.user.enums.Role;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Column(length = 32, nullable = false, unique = true)
    private String username;

    @GsonIgnore
    @Column(length = 32, nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private Instant createOn;

    @Column
    private Instant loggedOn;

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "id"))
    private Set<Role> roles = new HashSet<>();

    public User(String username, String password, boolean enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles.add(Role.Login);
        this.createOn = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

}
