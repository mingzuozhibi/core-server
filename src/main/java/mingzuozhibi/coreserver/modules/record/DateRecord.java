package mingzuozhibi.coreserver.modules.record;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mingzuozhibi.coreserver.commons.base.BaseModel;
import mingzuozhibi.coreserver.modules.disc.Disc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity(name = "record_date")
@Setter
@Getter
@NoArgsConstructor
public class DateRecord extends BaseModel implements Record {

    @ManyToOne(optional = false)
    private Disc disc;

    @Column(nullable = false)
    private LocalDate date;

    @Column
    private Double addPoint;

    @Column
    private Double sumPoint;

    @Column
    private Double powPoint;

    @Column
    private Double averRank;

}
