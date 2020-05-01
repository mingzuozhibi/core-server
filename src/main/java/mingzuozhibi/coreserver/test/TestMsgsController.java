package mingzuozhibi.coreserver.test;

import lombok.Data;
import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.commons.message.enums.Index;
import mingzuozhibi.coreserver.commons.message.enums.Level;
import mingzuozhibi.coreserver.commons.message.support.MsgsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestMsgsController extends BaseController {

    @Autowired
    private MsgsHelper msgsHelper;

    @Data
    private static class MessageForm {
        private Index index;
        private Level level;
        private String message;
    }

    @PostMapping("/test/msgs")
    public void sendMsgs(@RequestBody MessageForm form) {
        msgsHelper.sendMsg(form.index, form.level, form.message);
    }

    @PostMapping("/test/msgs/debug")
    public void sendMsgsDebug(@RequestBody MessageForm form) {
        msgsHelper.sendMsg(Index.Test, Level.DEBUG, form.message);
    }

}
