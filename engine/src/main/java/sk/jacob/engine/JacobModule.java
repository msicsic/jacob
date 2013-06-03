package sk.jacob.engine;

import sk.jacob.engine.types.DataPacket;

public interface JacobModule {
    DataPacket handle(DataPacket dataPacket);
}
