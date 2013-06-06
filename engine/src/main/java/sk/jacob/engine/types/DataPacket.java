package sk.jacob.engine.types;

public class DataPacket {
    public final MessageType message;

    public DataPacket(String rawRequest) {
        this.message = new MessageType();
        this.message.rawRequest = rawRequest;
    }
}
