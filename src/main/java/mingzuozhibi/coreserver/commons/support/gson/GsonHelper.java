package mingzuozhibi.coreserver.commons.support.gson;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import mingzuozhibi.coreserver.commons.support.Formatters;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;

public abstract class GsonHelper {

    public static final Gson GSON = createGson();

    public static Gson createGson() {
        GsonBuilder gson = new GsonBuilder();
        handleGsonIgnore(gson);
        registerInstant(gson);
        registerLocalDate(gson);
        return gson.create();
    }

    private static void handleGsonIgnore(GsonBuilder gson) {
        gson.setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getAnnotation(GsonIgnore.class) != null;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        });
    }

    private static void registerInstant(GsonBuilder gson) {
        gson.registerTypeAdapter(Instant.class, new TypeAdapter<Instant>() {
            @Override
            public void write(JsonWriter writer, Instant instant) throws IOException {
                if (instant != null) {
                    writer.value(instant.toEpochMilli());
                } else {
                    writer.nullValue();
                }
            }

            @Override
            public Instant read(JsonReader reader) throws IOException {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                    return null;
                } else {
                    return Instant.ofEpochMilli(reader.nextLong());
                }
            }
        });
    }

    private static void registerLocalDate(GsonBuilder gson) {
        gson.registerTypeAdapter(LocalDate.class, new TypeAdapter<LocalDate>() {
            @Override
            public void write(JsonWriter writer, LocalDate date) throws IOException {
                if (date != null) {
                    writer.value(date.format(Formatters.ISO_DATE_FORMATTER));
                } else {
                    writer.nullValue();
                }
            }

            @Override
            public LocalDate read(JsonReader reader) throws IOException {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                    return null;
                } else {
                    return LocalDate.parse(reader.nextString(), Formatters.ISO_DATE_FORMATTER);
                }
            }
        });
    }

}
