package mingzuozhibi.coreserver.commons.config;

import mingzuozhibi.coreserver.commons.support.Formatters;
import mingzuozhibi.coreserver.commons.support.page.PageParamsResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

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

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new PageParamsResolver());
    }

}
