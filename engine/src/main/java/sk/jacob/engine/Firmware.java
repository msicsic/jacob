package sk.jacob.engine;

import sk.jacob.types.DataPacket;

public interface Firmware {
    DataPacket handle(String portId, DataPacket dataPacket);
}
