package mingzuozhibi.coreserver.modules.logger.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface LoggerRepository extends JpaRepository<Logger, Long> {

    Page<Logger> findByModule(String module, Pageable pageable);

    Page<Logger> findByModuleAndLevelIn(String module, Set<String> level, Pageable pageable);

}
