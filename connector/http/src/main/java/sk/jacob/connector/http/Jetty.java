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
    private final String coreRoot;
    private final String guiRoot;
    private final String guiName;
    private final int serverPort;

    private Server server;
    private Bus bus;
    private String portId;

    public Jetty(String coreRoot, String guiRoot, String guiName, int serverPort) {
        this.coreRoot = coreRoot;
        this.guiRoot = guiRoot;
        this.guiName = guiName;
        this.serverPort = serverPort;
    }

    @Override
    public void init(String portId, Bus bus) {
        this.portId = portId;
        this.bus = bus;
    }

    @Override
    public void start() {
        this.server = init();
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

    private Server init() {
        Server server = new Server(this.serverPort);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] {
                configureGuiContextHandler(this.guiRoot, this.guiName),
                configureCoreContextHandler(this.coreRoot, this.portId, this.bus),
                configureRootHandler()
        });
        server.setHandler(handlers);

        return server;
    }

    private Handler configureGuiContextHandler(String guiRoot, String guiName) {
        ContextHandler context = new ContextHandler();
        context.setContextPath("/" + guiRoot);
        context.setClassLoader(Thread.currentThread().getContextClassLoader());

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});
        resourceHandler.setResourceBase(guiRoot + "/" + guiName);

        context.setHandler(resourceHandler);
        return context;
    }

    private Handler configureCoreContextHandler(String contextRoot, String portId, Bus bus) {
        ContextHandler context = new ContextHandler();
        context.setContextPath("/" + contextRoot);
        context.setClassLoader(Thread.currentThread().getContextClassLoader());
        context.setAllowNullPathInfo(true);

        CoreHandler coreHandler = new CoreHandler(portId, bus);
        context.setHandler(coreHandler);
        return context;
    }

    private Handler configureRootHandler() {
        RedirectPatternRule redirect = new RedirectPatternRule();
        redirect.setPattern("/");
        redirect.setLocation("/ui");

        RewriteHandler rewrite = new RewriteHandler();
        rewrite.addRule(redirect);
        return rewrite;
    }
}

