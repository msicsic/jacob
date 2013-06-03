package sk.jacob.engine.json;

import com.google.gson.*;

import java.lang.reflect.Type;

public class MessageTypeVersionDeserializer implements JsonDeserializer<String> {
    @Override
    public String deserialize(JsonElement jsonElement,
                              Type type,
                              JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jo = jsonElement.getAsJsonObject();
        return jo.get("type").getAsString() + "." + jo.get("version").getAsString();
    }
}
