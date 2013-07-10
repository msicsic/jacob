package sk.jacob.engine;

import sk.jacob.types.DataPacket;
import sk.jacob.util.Log;

import java.util.HashMap;
import java.util.Map;

import static sk.jacob.util.Log.logger;

public class Bus {
    private final Module firmware;
    private final Map<String, Connector> ports = new HashMap<String, Connector>();

    public Bus(Module firmware) {
        this.firmware = firmware;
    }

    public void attach(String portName, Connector connector) {
        ports.put(portName, connector);
    }

    public void start() {
        logger(this).info("Starting BUS ...");
        for (Map.Entry<String, Connector> port : ports.entrySet()) {
            logger(this).info("Starting PORT: " + port.getKey());
            Connector connector = port.getValue();
            connector.init(port.getKey(), this);
            connector.start();
        }
        logger(this).info("Bus started.");
    }

    public DataPacket send(String portId, DataPacket dataPacket) {
        logger(this).info(portId + " --->>> " + dataPacket.message.rawRequest);
        dataPacket = firmware.handle(dataPacket);
        logger(this).info(portId + " <<<--- " + dataPacket.message.rawResponse);
        return dataPacket;
    }
}
