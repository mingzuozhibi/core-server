package mingzuozhibi.coreserver.modules.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByModule(String module, Pageable pageable);

    Page<Message> findByModuleAndLevelIn(String module, Set<String> level, Pageable pageable);

}
