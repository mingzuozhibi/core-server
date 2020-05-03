package mingzuozhibi.coreserver.test.imports;

import lombok.extern.slf4j.Slf4j;
import mingzuozhibi.coreserver.modules.disc.DiscRepository;
import mingzuozhibi.coreserver.modules.group.Group;
import mingzuozhibi.coreserver.modules.group.GroupRepository;
import mingzuozhibi.coreserver.modules.group.enums.StatusType;
import mingzuozhibi.coreserver.modules.group.enums.UpdateType;
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
public class ImportGroups {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private DiscRepository discRepository;

    @GetMapping("/import/groups")
    public int importGroups() {
        AtomicInteger count = new AtomicInteger(0);
        jdbcTemplate.query("select * from mzzb_pro.disc_group", rs -> {
            Group group = new Group();
            group.setIndex(rs.getString("key"));
            group.setTitle(rs.getString("title"));
            group.setUpdate(getUpdate(rs.getBoolean("enabled")));
            group.setStatus(getStatus(rs.getInt("view_type"), group.getUpdate()));
            if (group.getStatus() == StatusType.Current) {
                group.setLastUpdate(getInstant(rs, "modify_time"));
            }
            jdbcTemplate.query("select d.asin as asin from mzzb_pro.disc_group_discs map" +
                    " left join mzzb_pro.disc d on map.disc_id = d.id" +
                    " where map.disc_group_id = ?",
                setter -> setter.setLong(1, rs.getLong("id")), rs2 -> {
                    discRepository.findByAsin(rs2.getString("asin")).ifPresent(disc -> {
                        group.getDiscs().add(disc);
                    });
                });
            group.setDiscCount(group.getDiscs().size());
            groupRepository.save(group);
            count.incrementAndGet();
        });
        return count.get();
    }

    private UpdateType getUpdate(boolean enabled) {
        return enabled ? UpdateType.Always : UpdateType.Never;
    }

    private StatusType getStatus(int viewType, UpdateType update) {
        if (viewType == 2) {
            return StatusType.Private;
        }
        if (update == UpdateType.Never) {
            return StatusType.History;
        }
        return StatusType.Current;
    }

    private Instant getInstant(ResultSet rs, String name) throws SQLException {
        return Optional.ofNullable(rs.getTimestamp(name)).map(Timestamp::toInstant).orElse(null);
    }

}
