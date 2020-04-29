package mingzuozhibi.coreserver.modules.group;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mingzuozhibi.coreserver.commons.base.BaseModel;
import mingzuozhibi.coreserver.commons.support.gson.GsonIgnore;
import mingzuozhibi.coreserver.modules.disc.Disc;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity(name = "group_")
@Setter
@Getter
@NoArgsConstructor
public class Group extends BaseModel {

    public enum StatusType {
        Current, History, Private
    }

    public enum UpdateType {
        Always, Until, Never
    }

    @Column(name = "index_", nullable = false, unique = true)
    private String index;

    @Column(name = "title_", nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_type", nullable = false, length = 20)
    private StatusType status;

    @Enumerated(EnumType.STRING)
    @Column(name = "update_type", nullable = false, length = 20)
    private UpdateType update;

    @Column(name = "update_date")
    private LocalDate updateDate;

    @Column(name = "last_update")
    private Instant lastUpdate;

    @Column(name = "disc_count", nullable = false)
    private Integer discCount;

    @GsonIgnore
    @ManyToMany
    @JoinTable(name = "group_discs",
        joinColumns = @JoinColumn(name = "id"),
        inverseJoinColumns = @JoinColumn(name = "disc_id"))
    private Set<Disc> discs = new HashSet<>();

    public boolean isNeedUpdate() {
        switch (update) {
            case Always:
                return true;
            case Never:
                return false;
            case Until:
                return Optional.ofNullable(updateDate)
                    .map(LocalDate.now()::isBefore)
                    .orElse(true);
            default:
                throw new IllegalStateException("Unexpected value: " + update);
        }
    }

}
