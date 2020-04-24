package mingzuozhibi.coreserver.modules.logs.log;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface LogRepository extends JpaRepository<Log, Long> {

    Page<Log> findByModule(String module, Pageable pageable);

    Page<Log> findByModuleAndLevelIn(String module, Set<String> level, Pageable pageable);

}
