package mingzuozhibi.coreserver.modules.group;

import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.commons.support.page.PageDefault;
import mingzuozhibi.coreserver.commons.support.page.PageParams;
import mingzuozhibi.coreserver.modules.disc.support.DiscPageSupport;
import mingzuozhibi.coreserver.modules.group.enums.StatusType;
import mingzuozhibi.coreserver.modules.group.enums.UpdateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupController extends BaseController {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private DiscPageSupport discPageSupport;

    @GetMapping("/api/groups")
    public String findAll(PageParams params) {
        return objectResult(groupRepository.findAll(params.toPageable()));
    }

    @GetMapping("/api/groups/{id}")
    public String findById(@PathVariable Long id) {
        return groupRepository.findById(id, this::objectResult);
    }

    @GetMapping("/api/groups/{id}/with/discs")
    public String findByIdWithDiscs(@PathVariable Long id,
                                    @PageDefault("rank") PageParams params) {
        return groupRepository.findById(id, group -> {
            var result = GSON.toJsonTree(group).getAsJsonObject();
            var filter = discPageSupport.filter(group.getDiscs(), params);
            result.add("discs", GSON.toJsonTree(filter));
            return objectResult(result, buildPage(params, group.getDiscCount()));
        });
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
