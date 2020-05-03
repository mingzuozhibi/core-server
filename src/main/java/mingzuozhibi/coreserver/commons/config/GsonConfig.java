package mingzuozhibi.coreserver.commons.config;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import mingzuozhibi.coreserver.commons.gson.GSONs;
import mingzuozhibi.coreserver.commons.gson.NotGson;
import mingzuozhibi.coreserver.commons.support.Formatters;
import mingzuozhibi.coreserver.modules.disc.Disc;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;

public abstract class GsonConfig {

    public static GsonBuilder basicBuilder() {
        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(new GsonIgnoreExclusionStrategy());
        builder.registerTypeAdapter(Instant.class, new InstantTypeAdapter());
        builder.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter());
        return builder;
    }

    public static GsonBuilder commonBuilder() {
        GsonBuilder builder = basicBuilder();
        builder.registerTypeAdapter(Disc.class, new DiscSerializer());
        return builder;
    }

    private static class GsonIgnoreExclusionStrategy implements ExclusionStrategy {
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getAnnotation(NotGson.class) != null;
        }

        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

    private static class InstantTypeAdapter extends TypeAdapter<Instant> {
        public void write(JsonWriter writer, Instant instant) throws IOException {
            if (instant != null) {
                writer.value(instant.toEpochMilli());
            } else {
                writer.nullValue();
            }
        }

        public Instant read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            } else {
                return Instant.ofEpochMilli(reader.nextLong());
            }
        }
    }

    private static class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {
        public void write(JsonWriter writer, LocalDate date) throws IOException {
            if (date != null) {
                writer.value(date.format(Formatters.ISO_DATE_FORMATTER));
            } else {
                writer.nullValue();
            }
        }

        public LocalDate read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            } else {
                return LocalDate.parse(reader.nextString(), Formatters.ISO_DATE_FORMATTER);
            }
        }
    }

    private static class DiscSerializer implements JsonSerializer<Disc> {
        public JsonElement serialize(Disc disc, Type type, JsonSerializationContext context) {
            JsonObject object = GSONs.BASIC.toJsonTree(disc).getAsJsonObject();
            object.addProperty("releaseDays", disc.findReleaseDays());
            return object;
        }
    }
}
