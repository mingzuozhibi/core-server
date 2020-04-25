package mingzuozhibi.coreserver.modules.message;

import mingzuozhibi.coreserver.commons.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Set;

@RestController
public class MessageController extends BaseController {

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/api/messages/{module}")
    public String findByModule(@PathVariable String module,
                               @RequestParam(required = false) Set<String> levels,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "50") int pageSize) {
        PageRequest pageable = PageRequest.of(page - 1, pageSize);
        if (levels == null || levels.isEmpty()) {
            return objectResult(messageRepository.findByModule(module, pageable));
        } else {
            return objectResult(messageRepository.findByModuleAndLevelIn(module, levels, pageable));
        }
    }

    @JmsListener(destination = "jms.msgs")
    @Transactional
    public void listenJmsMsgs(String json) {
        Message message = GSON.fromJson(json, Message.class);
        message.setAcceptOn(Instant.now());
        messageRepository.save(message);
    }

}
