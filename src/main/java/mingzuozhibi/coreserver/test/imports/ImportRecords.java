package mingzuozhibi.coreserver.test.imports;

import mingzuozhibi.coreserver.modules.disc.DiscRepository;
import mingzuozhibi.coreserver.modules.record.DateRecord;
import mingzuozhibi.coreserver.modules.record.DateRecordRepository;
import mingzuozhibi.coreserver.modules.record.HourRecord;
import mingzuozhibi.coreserver.modules.record.HourRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class ImportRecords {

    @Autowired
    private HourRecordRepository hourRecordRepository;

    @Autowired
    private DateRecordRepository dateRecordRepository;

    @Autowired
    private DiscRepository discRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/import/hourRecords")
    public long importHourRecords() {
        jdbcTemplate.query("select h.*, d.asin as asin from mzzb_pro.hour_record h " +
            "left join mzzb_pro.disc d on h.disc_id = d.id", rs -> {
            String asin = rs.getString("asin");
            discRepository.findByAsin(asin).ifPresent(disc -> {
                try {
                    HourRecord hourRecord = new HourRecord();
                    hourRecord.setDisc(disc);
                    hourRecord.setDate(rs.getDate("date").toLocalDate());
                    hourRecord.setAddPoint(getDouble(rs, "today_pt"));
                    hourRecord.setSumPoint(getDouble(rs, "total_pt"));
                    hourRecord.setPowPoint(getDouble(rs, "guess_pt"));
                    for (int h = 0; h < 24; h++) {
                        Integer rank = getInterger(rs, String.format("rank%02d", h));
                        hourRecord.getRankEmbedded().setRank(h, rank);
                    }
                    hourRecordRepository.save(hourRecord);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
        });
        return hourRecordRepository.count();
    }

    @GetMapping("/import/dateRecords")
    public long importDateRecords() {
        long total = discRepository.count();
        AtomicInteger count = new AtomicInteger();
        discRepository.findAll().forEach(disc -> {
            jdbcTemplate.query("select h.*, d.asin as asin from mzzb_pro.date_record h " +
                "left join mzzb_pro.disc d on h.disc_id = d.id " +
                "where d.asin = ?", ps -> ps.setString(1, disc.getAsin()), rs -> {
                try {
                    DateRecord dateRecord = new DateRecord();
                    dateRecord.setDisc(disc);
                    dateRecord.setDate(rs.getDate("date").toLocalDate());
                    dateRecord.setAddPoint(getDouble(rs, "today_pt"));
                    dateRecord.setSumPoint(getDouble(rs, "total_pt"));
                    dateRecord.setPowPoint(getDouble(rs, "guess_pt"));
                    dateRecord.setAverRank(getDouble(rs, "rank"));
                    dateRecordRepository.save(dateRecord);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
            System.out.printf("import: %d/%d%n", count.incrementAndGet(), total);
        });
        return dateRecordRepository.count();
    }

    private Double getDouble(ResultSet rs, String name) throws SQLException {
        return (Double) Optional.ofNullable(rs.getObject(name)).orElse(null);
    }

    private Integer getInterger(ResultSet rs, String name) throws SQLException {
        return Optional.ofNullable(rs.getObject(name))
            .map(o -> (Integer) o)
            .filter(i -> i != 0)
            .orElse(null);
    }

}
