package sk.jacob.common;

import java.util.HashMap;
import java.util.Map;
import sk.jacob.types.DataPacket;

public enum CONTEXT {
    CONNECTION;

    private static final String CONTEXT_KEY = "BUSSINESS_CONTEXT";

    public Object get(DataPacket dataPacket) {
        Map<String, Object> bc = dataPacket.context.get(CONTEXT_KEY);
        return bc.get(this.name());
    }

    public void set(DataPacket dataPacket, Object value) {
        if(dataPacket.context.containsKey(CONTEXT_KEY) == Boolean.FALSE) {
            dataPacket.context.put(CONTEXT_KEY, new HashMap<String, Object>());
        }
        Map<String, Object> bc = dataPacket.context.get(CONTEXT_KEY);
        bc.put(this.name(), value);
    }
}
