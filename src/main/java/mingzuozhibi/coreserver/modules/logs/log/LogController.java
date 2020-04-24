package mingzuozhibi.coreserver.modules.logs.log;

import mingzuozhibi.coreserver.commons.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class LogController extends BaseController {

    @Autowired
    private LogRepository logRepository;

    @GetMapping("/api/logs/{module}")
    public String findByModule(@PathVariable String module,
                               @RequestParam(required = false) Set<String> levels,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "50") int pageSize) {
        PageRequest pageable = PageRequest.of(page - 1, pageSize);
        if (levels == null || levels.isEmpty()) {
            return objectResult(logRepository.findByModule(module, pageable));
        } else {
            return objectResult(logRepository.findByModuleAndLevelIn(module, levels, pageable));
        }
    }

}
