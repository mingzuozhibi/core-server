package mingzuozhibi.coreserver.modules.record.date;

import mingzuozhibi.coreserver.modules.disc.Disc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DateRecordRepository extends JpaRepository<DateRecord, Long> {

    List<DateRecord> findByDiscOrderByDateDesc(Disc disc);

}
