package mingzuozhibi.coreserver.base;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class BaseController {

    public String objectResult(String message) {
        return objectResult(new JsonPrimitive(message));
    }

    public String objectResult(Number number) {
        return objectResult(new JsonPrimitive(number));
    }

    public String objectResult(Boolean bool) {
        return objectResult(new JsonPrimitive(bool));
    }

    public String objectResult(JsonElement element) {
        JsonObject root = new JsonObject();
        root.addProperty("success", true);
        root.add("content", element);
        return root.toString();
    }

    public String errorMessage(String message) {
        JsonObject root = new JsonObject();
        root.addProperty("success", false);
        root.addProperty("message", message);
        return root.toString();
    }

}
