package mingzuozhibi.coreserver.modules.auth.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mingzuozhibi.coreserver.commons.base.BaseModel;
import mingzuozhibi.coreserver.commons.gson.GsonIgnore;

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

    /**
     * Guest <br>
     * Login <br>
     * DiscAdmin <br>
     * UserAdmin <br>
     * RootAdmin <br>
     */

    public static final Set<String> ALL_ROLES = Set.of("Guest", "Login", "DiscAdmin", "UserAdmin", "RootAdmin");

    @ElementCollection
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    private Set<String> roles = new HashSet<>();

    public User(String username, String password, boolean enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles.add("Login");
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
