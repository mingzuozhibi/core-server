package mingzuozhibi.coreserver.modules.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByIndex(String index, Pageable pageable);

    Page<Message> findByIndexAndLevelIn(String index, Set<String> level, Pageable pageable);

}
