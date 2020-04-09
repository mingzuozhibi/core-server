package mingzuozhibi.coreserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/hello")
    public String hello(@RequestParam String name) {
        return String.format("Hello, %s", name);
    }

}
