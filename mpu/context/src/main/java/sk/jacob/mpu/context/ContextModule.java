package sk.jacob.mpu.context;

import sk.jacob.engine.Module;
import sk.jacob.engine.types.DataPacket;

public class ContextModule implements Module {
    @Override
    public DataPacket handle(DataPacket dataPacket) {
        return dataPacket;
    }
}
