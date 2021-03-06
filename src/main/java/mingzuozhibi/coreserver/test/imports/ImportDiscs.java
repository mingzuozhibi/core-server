package mingzuozhibi.coreserver.test.imports;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import mingzuozhibi.coreserver.modules.disc.Disc;
import mingzuozhibi.coreserver.modules.disc.DiscRepository;
import mingzuozhibi.coreserver.modules.disc.enums.DiscType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
public class ImportDiscs {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private DiscRepository discRepository;

    @GetMapping("/import/discs")
    public int importDiscs() {
        AtomicInteger count = new AtomicInteger(0);
        jdbcTemplate.query("select * from mzzb_pro.disc", rs -> {
            Disc disc = new Disc();
            disc.setAsin(rs.getString("asin"));
            disc.setTitle(rs.getString("title"));
            disc.setTitleCN(Strings.emptyToNull(rs.getString("title_pc")));
            disc.setThisRank(getInterger(rs, "this_rank"));
            disc.setPrevRank(getInterger(rs, "prev_rank"));
            disc.setAddPoint(getInterger(rs, "today_pt"));
            disc.setSumPoint(getInterger(rs, "total_pt"));
            disc.setPowPoint(getInterger(rs, "guess_pt"));
            disc.setCreateOn(getInstant(rs, "create_time"));
            disc.setUpdateOn(getInstant(rs, "update_time"));
            disc.setModifyOn(getInstant(rs, "modify_time"));
            disc.setDiscType(getDiscType(rs.getInt("disc_type")));
            disc.setReleaseDate(rs.getDate("release_date").toLocalDate());
            discRepository.save(disc);
            count.incrementAndGet();
        });
        return count.get();
    }

    private Instant getInstant(ResultSet rs, String name) throws SQLException {
        return Optional.ofNullable(rs.getTimestamp(name)).map(Timestamp::toInstant).orElse(null);
    }

    private Integer getInterger(ResultSet rs, String name) throws SQLException {
        return Optional.ofNullable(rs.getObject(name))
            .map(o -> (Integer) o)
            .filter(i -> i != 0)
            .orElse(null);
    }

    private DiscType getDiscType(int type) {
        switch (type) {
            case 0:
                return DiscType.CD;
            case 1:
                return DiscType.DVD;
            case 2:
                return DiscType.BD;
            case 3:
                return DiscType.BD_OR_DVD;
            default:
                return DiscType.OTHER;
        }
    }

}
