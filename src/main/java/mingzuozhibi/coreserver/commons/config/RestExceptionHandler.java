package mingzuozhibi.coreserver.commons.config;

import mingzuozhibi.coreserver.commons.support.ReturnUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import static mingzuozhibi.coreserver.commons.support.gson.GsonHelper.GSON;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public String servletExceptionHandler(HttpServletRequest request, Exception e) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String klass = e.getClass().getName();
        String message = e.getMessage();
        String error = String.format("%s '%s' %n%s: %s", method, uri, klass, message);
        return ReturnUtils.errorMessage(error, GSON.toJsonTree(e));
    }

}
