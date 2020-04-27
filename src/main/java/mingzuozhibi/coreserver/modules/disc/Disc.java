package mingzuozhibi.coreserver.modules.disc;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mingzuozhibi.coreserver.commons.base.BaseModel;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

@Entity(name = "disc")
@Getter
@Setter
@NoArgsConstructor
public class Disc extends BaseModel {

    public enum DiscType {
        CD, BD, DVD, BD_OR_DVD, OTHER
    }

    @Column(length = 20, nullable = false, unique = true)
    private String asin;

    @Column(length = 500, nullable = false)
    private String title;

    @Column
    private String titleCN;

    @Column
    private Integer thisRank;

    @Column
    private Integer prevRank;

    @Column
    private Integer addPoint;

    @Column
    private Integer sumPoint;

    @Column
    private Integer powPoint;

    @Column
    private Instant createOn;

    @Column
    private Instant updateOn;

    @Column
    private Instant modifyOn;

    @Enumerated(EnumType.STRING)
    private DiscType discType;

    @Column
    private LocalDate releaseDate;

    @Transient
    public Long getReleaseDays() {
        return Optional.ofNullable(releaseDate)
            .map(date -> date.toEpochDay() - LocalDate.now().toEpochDay())
            .orElse(null);
    }

}
