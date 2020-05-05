package mingzuozhibi.coreserver.modules.record;

import lombok.Getter;
import lombok.Setter;
import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.modules.disc.Disc;
import mingzuozhibi.coreserver.modules.disc.DiscRepository;
import mingzuozhibi.coreserver.modules.record.date.DateRecordRepository;
import mingzuozhibi.coreserver.modules.record.hour.HourRecordRepository;
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

    @GetMapping("/api/records/find/disc/{id}")
    public String findByDisc(@PathVariable Long id) {
        return discRepository.findById(id, disc -> {
            DiscVo vo = new DiscVo(disc);
            hourRecordRepository.findByDiscAndDateNow(disc).ifPresent(vo::add);
            dateRecordRepository.findByDiscOrderByDateDesc(disc).forEach(vo::add);
            return objectResult(vo);
        });
    }

    @Getter
    private static class DiscVo {
        private String title;
        private LocalDate release;
        private List<RecordVo> records = new LinkedList<>();
        private AtomicLong count = new AtomicLong();

        public DiscVo(Disc disc) {
            this.title = disc.findTitle();
            this.release = disc.getReleaseDate();
        }

        public void add(Record record) {
            RecordVo vo = new RecordVo();
            vo.setId(count.incrementAndGet());
            vo.setDate(record.getDate());
            vo.setAddPoint(record.getAddPoint());
            vo.setSumPoint(record.getSumPoint());
            vo.setPowPoint(record.getPowPoint());
            vo.setAverRank(record.getAverRank());
            records.add(vo);
        }

        @Setter
        @Getter
        private static class RecordVo {
            private Long id;
            private LocalDate date;
            private Double addPoint;
            private Double sumPoint;
            private Double powPoint;
            private Double averRank;
        }
    }

}
