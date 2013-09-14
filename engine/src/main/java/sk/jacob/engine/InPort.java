package sk.jacob.engine;

import sk.jacob.appcommon.accessor.COMMON;
import sk.jacob.appcommon.types.ExecutionContext;
import sk.jacob.appcommon.types.Message;

import java.util.ArrayList;
import java.util.List;

import static sk.jacob.common.util.Log.logger;

public class InPort {
    public final String id;
    private Bus bus;
    private final List<Connector> connectors = new ArrayList<>();

    public InPort(String portId) {
        this.id = portId;
    }

    public void associateWith(Bus bus) {
        this.bus = bus;
    }

    public void attach(Connector connector) {
        connector.associateWith(this);
        connectors.add(connector);
    }

    public Message onRequest(Message message) {
        logger(this).info(id + " --->>> " + message.rawRequest);
        ExecutionContext ec = new ExecutionContext();
        COMMON.MESSAGE.set(message, ec);
        ec = bus.onRequest(ec);
        logger(this).info(id + " <<<--- " + COMMON.MESSAGE.getFrom(ec).rawResponse);
        return COMMON.MESSAGE.getFrom(ec);
    }

    public void start() {
        for(Connector connector : connectors) {
            connector.start();
        }
    }
}
