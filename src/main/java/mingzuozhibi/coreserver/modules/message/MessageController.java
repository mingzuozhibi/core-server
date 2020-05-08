package mingzuozhibi.coreserver.modules.message;

import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.commons.support.QueueKeys;
import mingzuozhibi.coreserver.commons.support.page.PageDefault;
import mingzuozhibi.coreserver.commons.support.page.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Set;

import static mingzuozhibi.coreserver.commons.gson.GSONs.GSON;

@RestController
public class MessageController extends BaseController {

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/api/messages")
    public String findByIndex(@RequestParam(defaultValue = "Default") String index,
                              @RequestParam(required = false) Set<String> levels,
                              @PageDefault(size = 20, sort = "id,desc") PageParams params) {
        Pageable pageable = params.toPageable();
        if (levels == null || levels.isEmpty()) {
            return objectResult(messageRepository.findByIndex(index, pageable));
        } else {
            return objectResult(messageRepository.findByIndexAndLevelIn(index, levels, pageable));
        }
    }

    @Transactional
    @JmsListener(destination = QueueKeys.JMS_LOG_KEY)
    public void listenJmsLog(String json) {
        Message message = GSON.fromJson(json, Message.class);
        message.setAcceptOn(Instant.now());
        messageRepository.save(message);
    }

}
