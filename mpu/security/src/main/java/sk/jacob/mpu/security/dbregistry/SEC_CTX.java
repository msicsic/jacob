package sk.jacob.mpu.security.dbregistry;

import sk.jacob.engine.types.DataPacket;

public enum SEC_CTX {
    EXECUTION_CTX;

    public Object get(DataPacket dataPacket) {
        return dataPacket.security.context.get(this.toString());
    }
    public Object set(DataPacket dataPacket, Object value) {
        return dataPacket.security.context.put(this.toString(), value);
    }
}
