package sk.jacob.engine;

import sk.jacob.engine.types.DataPacket;

public interface Module {
    DataPacket handle(DataPacket dataPacket);
}
