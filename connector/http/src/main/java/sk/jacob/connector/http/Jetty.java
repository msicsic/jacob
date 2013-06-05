package sk.jacob.connector.http;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import sk.jacob.engine.Bus;
import sk.jacob.engine.Connector;

public class Jetty implements Connector {
    private Server server;
    private Bus bus;

    public Jetty() {
        this.server = init();
    }

    private Server init() {
        Server server = new Server(5555);

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(false);
        resource_handler.setWelcomeFiles(new String[]{ "index.html" });

        resource_handler.setResourceBase(".");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resource_handler });
        server.setHandler(handlers);

        return server;
    }

    @Override
    public void start() {
        try {
            this.server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        try {
            this.server.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setBus(Bus bus) {
        this.bus = bus;
    }
}

