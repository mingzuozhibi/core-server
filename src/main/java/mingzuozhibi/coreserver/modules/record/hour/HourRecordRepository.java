package mingzuozhibi.coreserver.modules.record.hour;

import mingzuozhibi.coreserver.modules.disc.Disc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface HourRecordRepository extends JpaRepository<HourRecord, Long> {

    Optional<HourRecord> findByDiscAndDate(Disc disc, LocalDate date);

    default Optional<HourRecord> findByDiscAndDateNow(Disc disc) {
        return findByDiscAndDate(disc, LocalDate.now());
    }

}
