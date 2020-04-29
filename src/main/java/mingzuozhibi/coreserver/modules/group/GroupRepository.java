package mingzuozhibi.coreserver.modules.group;

import mingzuozhibi.coreserver.commons.support.ReturnUtils;
import mingzuozhibi.coreserver.modules.group.enums.StatusType;
import mingzuozhibi.coreserver.modules.group.enums.UpdateType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByIndex(String index);

    List<Group> findByStatus(StatusType status);

    List<Group> findByUpdate(UpdateType update);

    default String findById(Long id, Function<Group, String> function) {
        Optional<Group> byId = findById(id);
        if (byId.isEmpty()) {
            return ReturnUtils.errorMessage("列表Id不存在");
        }
        return function.apply(byId.get());
    }

    default String findByIndex(String index, Function<Group, String> function) {
        Optional<Group> byIndex = findByIndex(index);
        if (byIndex.isEmpty()) {
            return ReturnUtils.errorMessage("列表Index不存在");
        }
        return function.apply(byIndex.get());
    }

}
