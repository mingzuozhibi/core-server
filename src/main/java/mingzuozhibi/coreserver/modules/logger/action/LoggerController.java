package mingzuozhibi.coreserver.modules.logger.action;

import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.modules.logger.domain.LoggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class LoggerController extends BaseController {

    @Autowired
    private LoggerRepository loggerRepository;

    @GetMapping("/api/logger/{module}")
    public String findByModule(@PathVariable String module,
                               @RequestParam(required = false) Set<String> levels,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "50") int pageSize) {
        PageRequest pageable = PageRequest.of(page - 1, pageSize);
        if (levels == null || levels.isEmpty()) {
            return objectResult(loggerRepository.findByModule(module, pageable));
        } else {
            return objectResult(loggerRepository.findByModuleAndLevelIn(module, levels, pageable));
        }
    }

}
