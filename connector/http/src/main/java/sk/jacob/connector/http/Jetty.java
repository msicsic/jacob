package sk.jacob.connector.http;

import org.eclipse.jetty.rewrite.handler.RedirectPatternRule;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import sk.jacob.engine.Bus;
import sk.jacob.engine.Connector;

public class Jetty implements Connector {
    private Server server;
    private Bus bus;

    private String GUI_ROOT = "ui";
    private String GUI_NAME = "magua";

    public Jetty() {
        this.server = init();
    }

    private Server init() {
        Server server = new Server(5558);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] {
                configureGuiContextHandler(GUI_ROOT, GUI_NAME),
                configureRootHandler()});
        server.setHandler(handlers);

        return server;
    }

    private Handler configureRootHandler() {
        RedirectPatternRule redirect = new RedirectPatternRule();
        redirect.setPattern("/");
        redirect.setLocation("/ui");

        RewriteHandler rewrite = new RewriteHandler();
        rewrite.addRule(redirect);
        return rewrite;
    }

    private Handler configureGuiContextHandler(String guiRoot, String guiName) {
        ContextHandler context = new ContextHandler();
        context.setContextPath("/" + guiRoot);
        context.setClassLoader(Thread.currentThread().getContextClassLoader());

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(new String[]{ "index.html" });
        resource_handler.setResourceBase(guiRoot + "/" + guiName);

        context.setHandler(resource_handler);
        return context;
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

