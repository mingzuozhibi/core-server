package mingzuozhibi.coreserver.commons.base;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import lombok.extern.slf4j.Slf4j;
import mingzuozhibi.coreserver.commons.support.ReturnUtils;
import mingzuozhibi.coreserver.commons.support.gson.GsonHelper;
import mingzuozhibi.coreserver.commons.support.page.PageParams;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class BaseController {

    protected Gson GSON = GsonHelper.GSON;

    @ResponseBody
    @ExceptionHandler
    public String errorHandler(Exception e) {
        log.debug("errorHandler", e);
        List<String> errors = new LinkedList<>();
        Throwable t = e;
        while (t != null) {
            errors.add(t.getClass().getSimpleName() + ": " + e.getMessage());
            t = t.getCause();
        }
        return errorMessage(String.join("  ====>  ", errors));
    }

    protected String errorMessage(String error) {
        return ReturnUtils.errorMessage(error);
    }

    protected String objectResult(Boolean bool) {
        return ReturnUtils.objectResult(bool);
    }

    protected String objectResult(Number number) {
        return ReturnUtils.objectResult(number);
    }

    protected String objectResult(String content) {
        return ReturnUtils.objectResult(content);
    }

    protected String objectResult(Object data) {
        return ReturnUtils.objectResult(data);
    }

    protected String objectResult(Page<?> page) {
        return ReturnUtils.objectResult(page);
    }

    public static String objectResult(JsonElement data, JsonElement page) {
        return ReturnUtils.objectResult(data, page);
    }

    public static JsonElement buildPage(PageParams params, Integer totalElements) {
        return ReturnUtils.buildPage(params, totalElements);
    }

}
