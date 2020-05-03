package mingzuozhibi.coreserver.modules.record;

import lombok.Getter;
import lombok.Setter;
import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.modules.disc.Disc;
import mingzuozhibi.coreserver.modules.disc.DiscRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class RecordController extends BaseController {

    @Autowired
    private HourRecordRepository hourRecordRepository;

    @Autowired
    private DateRecordRepository dateRecordRepository;

    @Autowired
    private DiscRepository discRepository;

    @Getter
    private static class Result {
        private String title;
        private LocalDate release;
        private List<Record_> records = new LinkedList<>();
        private AtomicLong count = new AtomicLong();

        public Result(Disc disc) {
            this.title = disc.findTitle();
            this.release = disc.getReleaseDate();
        }

        public void add(Record record) {
            Record_ record_ = new Record_();
            record_.setId(count.incrementAndGet());
            record_.setDate(record.getDate());
            record_.setAddPoint(record.getAddPoint());
            record_.setSumPoint(record.getSumPoint());
            record_.setPowPoint(record.getPowPoint());
            record_.setAverRank(record.getAverRank());
            records.add(record_);
        }
    }

    @Setter
    @Getter
    private static class Record_ {
        private Long id;
        private LocalDate date;
        private Double addPoint;
        private Double sumPoint;
        private Double powPoint;
        private Double averRank;
    }

    @GetMapping("/api/records/find/disc/{id}")
    public String findByDisc(@PathVariable Long id) {
        return discRepository.findById(id, disc -> {
            Result result = new Result(disc);
            hourRecordRepository.findByDiscAndDateNow(disc).ifPresent(result::add);
            dateRecordRepository.findByDiscOrderByDateDesc(disc).forEach(result::add);
            return objectResult(result);
        });
    }

}
