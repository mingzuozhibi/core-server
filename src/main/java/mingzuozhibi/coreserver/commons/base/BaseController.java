package mingzuozhibi.coreserver.commons.base;

import com.google.gson.Gson;
import mingzuozhibi.coreserver.commons.gson.GsonHelper;
import mingzuozhibi.coreserver.commons.msgs.support.MsgsHelper;
import mingzuozhibi.coreserver.commons.util.ReturnUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedList;
import java.util.List;

public class BaseController {

    protected Gson GSON = GsonHelper.GSON;

    @Autowired
    protected MsgsHelper msgsHelper;

    @ResponseBody
    @ExceptionHandler
    public String errorHandler(Exception e) {
        List<String> errors = new LinkedList<>();
        Throwable t = e;
        while (t != null) {
            errors.add(t.getClass().getSimpleName() + ": " + e.getMessage());
            t = t.getCause();
        }
        return errorMessage(String.join(", ", errors));
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
