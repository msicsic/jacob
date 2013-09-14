package sk.jacob.engine;

import sk.jacob.appcommon.types.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

import static sk.jacob.common.util.Log.logger;

public class Bus {
    private final Application application;
    private final Map<String, InPort> inPorts = new HashMap<>();
    private final Map<String, InPort> outPorts = new HashMap<>();

    public Bus(Application application) {
        this.application = application;
    }

    public void attach(InPort inPort) {
        inPort.associateWith(this);
        inPorts.put(inPort.id, inPort);
    }

    public void start() {
        logger(this).info("Starting BUS ...");
        for (InPort inPort : inPorts.values()) {
            logger(this).info("Starting CONNECTOR: " + inPort.id);
            inPort.start();
        }
        logger(this).info("Bus started.");
    }

    public ExecutionContext onRequest(ExecutionContext ec) {
        return application.onRequest(ec);
    }
}
