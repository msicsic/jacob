package sk.jacob.engine.types;

import com.google.gson.JsonParser;

public class DataPacket {
    private static final JsonParser JSON_PARSER = new JsonParser();
    public final MessageType message;
    public SecurityType security = new SecurityType();

    public DataPacket(String rawRequest) {
        this.message = new MessageType();
        this.message.rawRequest = rawRequest;
        this.message.jsonRequest = JSON_PARSER.parse(rawRequest).getAsJsonObject();
    }
}
