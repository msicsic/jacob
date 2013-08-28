package sk.jacob.common;

import sk.jacob.types.DataPacket;
import sk.jacob.types.MessageType;

import java.util.HashMap;
import java.util.Map;

public enum MESSAGE {
    CURRENT;

    private static final String CONTEXT_KEY = "MESSAGE_CONTEXT";

    public Object get(DataPacket dataPacket) {
        Map<String, Object> bc = dataPacket.CONTEXT.get(CONTEXT_KEY);
        return bc.get(this.name());
    }

    public void set(DataPacket dataPacket, Object value) {
        if(dataPacket.CONTEXT.containsKey(CONTEXT_KEY) == false) {
            dataPacket.CONTEXT.put(CONTEXT_KEY, new HashMap<String, Object>());
        }
        Map<String, Object> bc = dataPacket.CONTEXT.get(CONTEXT_KEY);
        bc.put(this.name(), value);
    }

    public static MessageType current(DataPacket dataPacket) {
        return (MessageType)MESSAGE.CURRENT.get(dataPacket);
    }

    public static DataPacket createDataPacket(String rawRequest) {
        DataPacket dataPacket = new DataPacket();
        MessageType message;
        message = new MessageType();
        message.rawRequest = rawRequest;
        MESSAGE.CURRENT.set(dataPacket, message);
        return dataPacket;
    }

}
