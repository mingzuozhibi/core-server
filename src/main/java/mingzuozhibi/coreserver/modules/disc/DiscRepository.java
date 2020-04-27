package mingzuozhibi.coreserver.modules.disc;

import mingzuozhibi.coreserver.commons.util.ReturnUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface DiscRepository extends JpaRepository<Disc, Long> {

    Optional<Disc> findByAsin(String asin);

    List<Disc> findByTitleContains(String title, Pageable pageable);

    default String findById(Long id, Function<Disc, String> function) {
        Optional<Disc> byId = findById(id);
        if (byId.isEmpty()) {
            return ReturnUtils.errorMessage("碟片Id不存在");
        }
        return function.apply(byId.get());
    }

    default String findByAsin(String asin, Function<Disc, String> function) {
        Optional<Disc> byId = findByAsin(asin);
        if (byId.isEmpty()) {
            return ReturnUtils.errorMessage("碟片Asin不存在");
        }
        return function.apply(byId.get());
    }

}
