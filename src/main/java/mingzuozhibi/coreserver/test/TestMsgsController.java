package mingzuozhibi.coreserver.test;

import lombok.Data;
import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.commons.msgs.Msgs;
import mingzuozhibi.coreserver.commons.msgs.MsgsWired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestMsgsController extends BaseController {

    @MsgsWired(Msgs.Tag.Test)
    private Msgs msgs;

    @Data
    private static class MessageForm {
        private String message;
    }

    @PostMapping("/test/info")
    public void info(@RequestBody MessageForm form) {
        msgs.info(form.message);
    }

    @PostMapping("/test/debug")
    public void debug(@RequestBody MessageForm form) {
        msgs.debug(form.message);
    }

}
