package mingzuozhibi.coreserver.test;

import mingzuozhibi.coreserver.base.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class TestController extends BaseController {

    @GetMapping("/api/hello")
    public String hello(@RequestParam String name) {
        if (Objects.equals(name, "jack")) {
            return errorMessage("Can't access");
        }
        return objectResult("hello " + name);
    }

}
