package sk.jacob.engine;

import sk.jacob.common.MESSAGE;
import sk.jacob.types.DataPacket;
import java.util.ArrayList;
import java.util.List;

import static sk.jacob.util.Log.logger;

public class Bus {
    private final Module firmware;
    private final List<Connector> ports = new ArrayList<>();

    public Bus(Module firmware) {
        this.firmware = firmware;
    }

    public void attach(String portName, Connector connector) {
        connector.init(portName, this);
        ports.add(connector);
    }

    public void start() {
        logger(this).info("Starting BUS ...");
        for (Connector connector : ports) {
            logger(this).info("Starting CONNECTOR: " + connector.portId());
            connector.start();
        }
        logger(this).info("Bus started.");
    }

    public DataPacket send(String portId, DataPacket dataPacket) {
        logger(this).info(portId + " --->>> " + MESSAGE.current(dataPacket).rawRequest);
        // TODO: PortID is not used now.
        dataPacket = firmware.handle(dataPacket);
        logger(this).info(portId + " <<<--- " + MESSAGE.current(dataPacket).rawResponse);
        return dataPacket;
    }
}
