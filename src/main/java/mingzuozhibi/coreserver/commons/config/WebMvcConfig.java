package mingzuozhibi.coreserver.commons.config;

import mingzuozhibi.coreserver.commons.util.Formatters;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Instant;
import java.time.LocalDate;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

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
