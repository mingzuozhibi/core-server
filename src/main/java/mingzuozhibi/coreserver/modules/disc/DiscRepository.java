package mingzuozhibi.coreserver.modules.disc;

import mingzuozhibi.coreserver.commons.util.ReturnUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.function.Function;

public interface DiscRepository extends JpaRepository<Disc, Long>, JpaSpecificationExecutor<Disc> {

    Optional<Disc> findByAsin(String asin);

    Page<Disc> findByTitleContains(String title, Pageable pageable);

    Page<Disc> findByTitleCNContains(String titleCN, Pageable pageable);

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
