package sk.jacob.mpu.security;

import sk.jacob.engine.Module;
import sk.jacob.engine.types.DataPacket;

public class SecurityModule implements Module {
    @Override
    public DataPacket handle(DataPacket dataPacket) {
        return dataPacket;
    }
}
