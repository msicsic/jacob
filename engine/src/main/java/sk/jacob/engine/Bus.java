package sk.jacob.engine;

import sk.jacob.types.ExecutionContext;
import sk.jacob.types.Message;

import java.util.ArrayList;
import java.util.List;

import static sk.jacob.util.Log.logger;

public class Bus {
    private final Firmware firmware;
    private final List<Connector> ports = new ArrayList<>();

    public Bus(Firmware firmware) {
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

    public ExecutionContext send(String portId, Message message) {
        return firmware.handle(portId, message);
    }
}
