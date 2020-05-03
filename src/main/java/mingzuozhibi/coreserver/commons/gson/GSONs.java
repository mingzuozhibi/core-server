package mingzuozhibi.coreserver.commons.gson;

import com.google.gson.Gson;
import mingzuozhibi.coreserver.commons.config.GsonConfig;

public abstract class GSONs {

    public static Gson BASIC = GsonConfig.basicBuilder().create();
    public static Gson GSON = GsonConfig.commonBuilder().create();

}
