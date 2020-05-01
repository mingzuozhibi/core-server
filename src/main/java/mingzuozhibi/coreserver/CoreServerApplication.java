package mingzuozhibi.coreserver;

import mingzuozhibi.coreserver.commons.support.ReflectUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableScheduling
@SpringBootApplication
public class CoreServerApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        ReflectUtils.disableAccessWarnings();
        SpringApplication.run(CoreServerApplication.class, args);
    }

}
