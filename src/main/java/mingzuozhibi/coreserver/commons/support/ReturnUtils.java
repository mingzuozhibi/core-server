package mingzuozhibi.coreserver.commons.support;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import mingzuozhibi.coreserver.commons.support.page.PageParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;

import static mingzuozhibi.coreserver.commons.support.gson.GsonHelper.GSON;

public abstract class ReturnUtils {

    public static String errorMessage(String error) {
        Objects.requireNonNull(error);
        JsonObject root = new JsonObject();
        root.addProperty("success", false);
        root.addProperty("message", error);
        return root.toString();
    }

    public static String objectResult(Boolean bool) {
        Objects.requireNonNull(bool);
        return objectResult(new JsonPrimitive(bool));
    }

    public static String objectResult(Number number) {
        Objects.requireNonNull(number);
        return objectResult(new JsonPrimitive(number));
    }

    public static String objectResult(String content) {
        Objects.requireNonNull(content);
        return objectResult(new JsonPrimitive(content));
    }

    public static String objectResult(Object data) {
        Objects.requireNonNull(data);
        return objectResult(GSON.toJsonTree(data));
    }

    private static String objectResult(JsonElement data) {
        Objects.requireNonNull(data);
        JsonObject root = new JsonObject();
        root.addProperty("success", true);
        root.add("data", data);
        return root.toString();
    }

    public static String objectResult(Page<?> page) {
        Objects.requireNonNull(page);
        List<?> data = page.getContent();
        Objects.requireNonNull(data);
        return objectResult(GSON.toJsonTree(data), buildPage(page));
    }

    public static String objectResult(JsonElement data, JsonElement page) {
        JsonObject root = new JsonObject();
        root.addProperty("success", true);
        root.add("data", data);
        root.add("page", page);
        return root.toString();
    }

    public static JsonElement buildPage(Page<?> page) {
        Pageable pageable = page.getPageable();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber() + 1;
        long totalElements = page.getTotalElements();
        return buildPage(currentPage, pageSize, totalElements);
    }

    public static JsonElement buildPage(PageParams params, Integer totalElements) {
        return buildPage(params.getPage(), params.getSize(), totalElements);
    }

    public static JsonElement buildPage(long currentPage, long pageSize, long totalElements) {
        JsonObject object = new JsonObject();
        object.addProperty("pageSize", pageSize);
        object.addProperty("currentPage", currentPage);
        object.addProperty("totalElements", totalElements);
        return object;
    }

}
