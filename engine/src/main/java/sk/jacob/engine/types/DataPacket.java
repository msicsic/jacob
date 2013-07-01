package sk.jacob.engine.types;

import com.google.gson.JsonParser;

public class DataPacket {
    public final MessageType message;
    public STATUS status = STATUS.AFP;
    public SecurityType security = new SecurityType();

    public DataPacket(String rawRequest) {
        this.message = new MessageType();
        this.message.rawRequest = rawRequest;
    }
}
