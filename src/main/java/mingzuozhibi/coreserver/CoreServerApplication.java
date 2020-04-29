package mingzuozhibi.coreserver;

import mingzuozhibi.coreserver.commons.support.Formatters;
import mingzuozhibi.coreserver.commons.support.ReflectUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.format.FormatterRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Instant;
import java.time.LocalDate;

@EnableScheduling
@SpringBootApplication
public class CoreServerApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        ReflectUtils.disableAccessWarnings();
        SpringApplication.run(CoreServerApplication.class, args);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(String.class, LocalDate.class,
            source -> LocalDate.parse(source, Formatters.ISO_DATE_FORMATTER)
        );
        registry.addConverter(String.class, Instant.class,
            source -> Instant.ofEpochMilli(Long.parseLong(source))
        );
    }

}
