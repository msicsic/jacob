package sk.jacob.mpu.context;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import sk.jacob.engine.types.DataPacket;

public enum Context {
    EXECUTION_CTX;

    private static final Map<Integer, Map<Context, Object>> CONTEXT_MAP = new ConcurrentHashMap<>();

    private Map<Context, Object> getDataPacketContextMap(DataPacket dataPacket) {
        Integer dataPacketHash = dataPacket.hashCode();
        Map<Context, Object> map;
        if (CONTEXT_MAP.containsKey(dataPacketHash)) {
            map = CONTEXT_MAP.get(dataPacketHash);
        } else {
            map = new HashMap<>();
            CONTEXT_MAP.put(dataPacketHash, map);
        }
        return map;
    }

    public Object get(DataPacket dataPacket) {
        return getDataPacketContextMap(dataPacket).get(this);
    }

    void set(DataPacket dataPacket, Object value) {
        getDataPacketContextMap(dataPacket).put(this, value);
    }

    static void clear(DataPacket dataPacket) {
        CONTEXT_MAP.remove(dataPacket.hashCode());
    }
}
