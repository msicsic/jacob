package sk.jacob.engine;

import sk.jacob.types.DataPacket;

public interface Module {
    DataPacket handle(DataPacket dataPacket);
}
