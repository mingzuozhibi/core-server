package mingzuozhibi.coreserver.modules.group;

import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.modules.group.enums.StatusType;
import mingzuozhibi.coreserver.modules.group.enums.UpdateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupController extends BaseController {

    @Autowired
    private GroupRepository groupRepository;

    @GetMapping("/api/groups")
    public String findAll(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "50") int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        return objectResult(groupRepository.findAll(pageRequest));
    }

    @GetMapping("/api/groups/{id}")
    public String findById(@PathVariable Long id) {
        return groupRepository.findById(id, this::objectResult);
    }

    @GetMapping("/api/groups/find/index/{index}")
    public String findById(@PathVariable String index) {
        return groupRepository.findByIndex(index, this::objectResult);
    }

    @GetMapping("/api/groups/find/status/{status}")
    public String findByStatus(@PathVariable StatusType status) {
        return objectResult(groupRepository.findByStatus(status));
    }

    @GetMapping("/api/groups/find/update/{update}")
    public String findByStatus(@PathVariable UpdateType update) {
        return objectResult(groupRepository.findByUpdate(update));
    }

}
