package mingzuozhibi.coreserver.commons.config;

import lombok.extern.slf4j.Slf4j;
import mingzuozhibi.coreserver.commons.support.Formatters;
import mingzuozhibi.coreserver.commons.support.ReturnUtils;
import mingzuozhibi.coreserver.commons.support.page.PageParamsResolver;
import mingzuozhibi.coreserver.security.support.SecurityUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static mingzuozhibi.coreserver.commons.gson.GSONs.GSON;

@Slf4j
@Configuration
@RestControllerAdvice
public class WebMvcConfig implements WebMvcConfigurer {

    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> exceptionHandler(HttpServletRequest request, Exception e) {
        String username = SecurityUtils.getCurrentUsername();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String klass = e.getClass().getName();
        String message = e.getMessage();
        String error = String.format("(%s)%s '%s' %s: %s", username, method, uri, klass, message);
        return responseError(error, e);
    }

    private ResponseEntity<String> responseError(String error, Exception e) {
        if (e instanceof SecurityException || e instanceof AccessDeniedException) {
            log.debug(error);
        } else {
            log.debug(error, e);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = ReturnUtils.errorMessage(error, GSON.toJsonTree(e));
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
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

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new PageParamsResolver());
    }

}
