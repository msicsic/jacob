package sk.jacob.engine.types;

public class DataPacket {
    public final MessageType message;
    public DATAPACKET_STATUS dataPacketStatus = DATAPACKET_STATUS.AFP;
    public SecurityType security = new SecurityType();

    public DataPacket(String rawRequest) {
        this.message = new MessageType();
        this.message.rawRequest = rawRequest;
    }
}
