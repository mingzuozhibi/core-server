package mingzuozhibi.coreserver.commons.base;

import com.google.gson.Gson;
import mingzuozhibi.coreserver.commons.gson.GsonHelper;
import mingzuozhibi.coreserver.commons.util.ReturnUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class BaseController {

    protected Gson GSON = GsonHelper.GSON;

    @ResponseBody
    @ExceptionHandler
    public String errorHandler(Exception e) {
        return errorMessage(e.getMessage());
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

}
