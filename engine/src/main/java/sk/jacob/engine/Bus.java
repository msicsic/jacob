package sk.jacob.engine;

import sk.jacob.appcommon.accessor.COMMON;
import sk.jacob.appcommon.types.ExecutionContext;
import sk.jacob.appcommon.types.Message;

import java.util.HashMap;
import java.util.Map;

import static sk.jacob.common.util.Log.logger;

public class Bus {
    private final Application application;
    private final Map<String, InPort> inPortResources = new HashMap<>();
    private final Map<String, OutPort> outPortResources = new HashMap<>();

    public Bus(Application application) {
        this.application = application;
    }

    public void attach(InPort inPort) {
        inPort.associateWith(this);
        inPortResources.put("/RESOURCES/PORTS/IN/" + inPort.id, inPort);
    }

    public void attach(OutPort outPort) {
        outPortResources.put("/RESOURCES/PORTS/OUT/" + outPort.id, outPort);
    }

    public void start() {
        logger(this).info("Starting BUS ...");
        for (InPort inPort : inPortResources.values()) {
            logger(this).info("Starting CONNECTOR: " + inPort.id);
            inPort.start();
        }
        for (OutPort outPort : outPortResources.values()) {
            logger(this).info("Starting CONNECTOR: " + outPort.id);
            outPort.start();
        }
        logger(this).info("Bus started.");
    }

    public Message onRequest(Message message) {
        ExecutionContext ec = new ExecutionContext();
        COMMON.MESSAGE.storeValue(message, ec);
        injectPortResources(ec);
        ec = application.onRequest(ec);
        return COMMON.MESSAGE.getFrom(ec);
    }

    private void injectPortResources(ExecutionContext ec) {
        ec.INSTANCE.putAll(inPortResources);
        ec.INSTANCE.putAll(outPortResources);
    }
}
