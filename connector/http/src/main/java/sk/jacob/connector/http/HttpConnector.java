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
import sk.jacob.engine.InPort;

import java.util.Properties;

public class HttpConnector implements Connector {
    private final String httpPortContextPath;
    private final int httpPortPort;

    private final String guiRootPath;
    private final String guiName;

    private Server connectorInstance;
    private InPort inPort;

    private static final String GUI_CONTEXT_PATH = "/ui";

    public HttpConnector(Properties config) {
        this(config.getProperty("http_port.context_path"),
             Integer.valueOf(config.getProperty("http_port.port")),
             config.getProperty("gui.root_path"),
             config.getProperty("gui.name"));
    }

    public HttpConnector(String httpPortContextPath, int httpPortPort, String guiRootPath, String guiName) {
        this.httpPortContextPath = httpPortContextPath;
        this.guiRootPath = guiRootPath;
        this.guiName = guiName;
        this.httpPortPort = httpPortPort;
    }

    @Override
    public void associateWith(InPort inPort) {
        this.inPort = inPort;
    }

    @Override
    public void start() {
        if (this.connectorInstance != null)
            return;
        this.connectorInstance = getServer();
        try {
            this.connectorInstance.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        try {
            this.connectorInstance.stop();
            this.connectorInstance.join();
            this.connectorInstance = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Server getServer() {
        Server server = new Server(this.httpPortPort);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] {
                configureGuiContextHandler(),
                configureCoreContextHandler(),
                configureRootHandler()
        });

        server.setHandler(handlers);
        return server;
    }

    private Handler configureGuiContextHandler() {
        ContextHandler context = new ContextHandler();
        context.setContextPath(GUI_CONTEXT_PATH);
        context.setClassLoader(Thread.currentThread().getContextClassLoader());

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});
        resourceHandler.setResourceBase(this.guiRootPath + "/" + this.guiName);

        context.setHandler(resourceHandler);
        return context;
    }

    private Handler configureCoreContextHandler() {
        ContextHandler context = new ContextHandler();
        context.setContextPath(this.httpPortContextPath);
        context.setClassLoader(Thread.currentThread().getContextClassLoader());
        context.setAllowNullPathInfo(true);

        CoreHandler coreHandler = new CoreHandler(this.inPort);
        context.setHandler(coreHandler);
        return context;
    }

    private Handler configureRootHandler() {
        RedirectPatternRule redirect = new RedirectPatternRule();
        redirect.setPattern("/");
        redirect.setLocation(GUI_CONTEXT_PATH);

        RewriteHandler rewrite = new RewriteHandler();
        rewrite.addRule(redirect);
        return rewrite;
    }
}

