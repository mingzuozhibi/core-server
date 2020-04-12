package mingzuozhibi.coreserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CoreServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreServerApplication.class, args);
    }

}
