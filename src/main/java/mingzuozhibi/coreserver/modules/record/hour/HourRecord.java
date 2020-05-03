package mingzuozhibi.coreserver.modules.record.hour;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mingzuozhibi.coreserver.commons.base.BaseModel;
import mingzuozhibi.coreserver.modules.disc.Disc;
import mingzuozhibi.coreserver.modules.record.Record;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.OptionalDouble;
import java.util.stream.Stream;

@Entity(name = "record_hour")
@Setter
@Getter
@NoArgsConstructor
public class HourRecord extends BaseModel implements Record {

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

    @Embedded
    private HourRecordEmbedded rankEmbedded = new HourRecordEmbedded();

    @Transient
    public Double getAverRank() {
        Stream.Builder<Integer> builder = Stream.builder();
        for (int hour = 0; hour < 24; hour++) {
            builder.accept(rankEmbedded.getRank(hour));
        }
        OptionalDouble average = builder.build()
            .mapToInt(Integer::intValue)
            .average();
        return average.isEmpty() ? null : average.getAsDouble();
    }

}
