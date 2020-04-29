package mingzuozhibi.coreserver.test;

import lombok.Data;
import mingzuozhibi.coreserver.commons.base.BaseController;
import mingzuozhibi.coreserver.commons.message.enums.Tag;
import mingzuozhibi.coreserver.commons.message.support.MsgsHelper;
import mingzuozhibi.coreserver.commons.message.enums.Level;
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
        private Tag tag;
        private Level level;
        private String message;
    }

    @PostMapping("/test/msgs")
    public void sendMsgs(@RequestBody MessageForm form) {
        msgsHelper.sendMsg(form.tag, form.level, form.message);
    }

    @PostMapping("/test/msgs/debug")
    public void sendMsgsDebug(@RequestBody MessageForm form) {
        msgsHelper.sendMsg(Tag.Test, Level.DEBUG, form.message);
    }

}
