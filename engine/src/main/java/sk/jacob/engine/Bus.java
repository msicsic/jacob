package sk.jacob.engine;

import sk.jacob.appcommon.accessor.COMMON;
import sk.jacob.appcommon.types.IBus;
import sk.jacob.appcommon.types.ExecutionContext;

import java.util.ArrayList;
import java.util.List;

import static sk.jacob.common.util.Log.logger;

public class Bus implements IBus {
    private final Application application;
    private final List<Connector> ports = new ArrayList<>();

    public Bus(Application application) {
        this.application = application;
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

    @Override
    public ExecutionContext send(String portId, ExecutionContext ec) {
        COMMON.EMITTER.set(this, ec);
        return application.handle(portId, ec);
    }
}
