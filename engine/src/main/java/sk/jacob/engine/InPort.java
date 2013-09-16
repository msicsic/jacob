package sk.jacob.engine;

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

    public String onRequest(String rawMessage) {
        logger(this).info(id + " --->>> " + rawMessage);
        Message message = Message.getInstance(rawMessage);
        message = bus.onRequest(message);
        String rawResponse = message.getRawResponse();
        logger(this).info(id + " <<<--- " + rawResponse);
        return rawResponse;
    }

    public void start() {
        for(Connector connector : connectors) {
            connector.start();
        }
    }
}
