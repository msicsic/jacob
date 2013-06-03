package sk.jacob.engine;

import sk.jacob.engine.types.DataPacket;

public class EngineConfig implements JacobModule {
    @Override
    public DataPacket handle(DataPacket dataPacket) {
        return dataPacket;
    }
}
