package sk.jacob.engine;

import sk.jacob.engine.types.DataPacket;

import java.util.HashMap;
import java.util.Map;

import static sk.jacob.util.Log.sout;

public class Bus {
    private final Firmware firmware;
    private final Map<String, Connector> ports = new HashMap<String, Connector>();

    public Bus(Firmware firmware) {
        this.firmware = firmware;
    }

    public void attach(String portName, Connector connector) {
        ports.put(portName, connector);
    }

    public void start() {
        sout("Starting BUS ...");
        for (Map.Entry<String, Connector> port : ports.entrySet()) {
            sout("Starting PORT: " + port.getKey());
            Connector connector = port.getValue();
            connector.setBus(this);
            connector.start();
        }
        sout("Bus started.");
    }

    public DataPacket send(DataPacket dataPacket) {
        return dataPacket;
    }
}
