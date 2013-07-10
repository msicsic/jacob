package sk.jacob.types;

import java.util.HashMap;
import java.util.Map;

public class DataPacket {
    public final MessageType message;
    public DATAPACKET_STATUS dataPacketStatus = DATAPACKET_STATUS.AFP;
    public Map<String, Map<String, Object>> context = new HashMap<>();
    //public SecurityType security = new SecurityType();

    public DataPacket(String rawRequest) {
        this.message = new MessageType();
        this.message.rawRequest = rawRequest;
    }
}
