package sk.jacob.engine;

import sk.jacob.types.DataPacket;

public class EngineConfig implements Module {
    @Override
    public DataPacket handle(DataPacket dataPacket) {
        return dataPacket;
    }
}
