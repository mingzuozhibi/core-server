package mingzuozhibi.coreserver.modules.record;

import lombok.Getter;
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

@RestController
public class RecordController extends BaseController {

    @Autowired
    private HourRecordRepository hourRecordRepository;

    @Autowired
    private DateRecordRepository dateRecordRepository;

    @Autowired
    private DiscRepository discRepository;

    @Getter
    private static class Data {
        private String title;
        private LocalDate release;
        private List<Record> records = new LinkedList<>();

        public Data(Disc disc) {
            this.title = disc.findTitle();
            this.release = disc.getReleaseDate();
        }
    }

    @GetMapping("/api/records/find/disc/{id}")
    public String findByDisc(@PathVariable Long id) {
        return discRepository.findById(id, disc -> {
            Data data = new Data(disc);
            hourRecordRepository.findByDiscAndDateNow(disc).ifPresent(data.getRecords()::add);
            data.getRecords().addAll(dateRecordRepository.findByDiscOrderByDateDesc(disc));
            return objectResult(data);
        });
    }

}
