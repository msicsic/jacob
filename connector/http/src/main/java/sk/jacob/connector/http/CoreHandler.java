package sk.jacob.connector.http;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import sk.jacob.engine.Bus;
import sk.jacob.types.DataPacket;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CoreHandler extends AbstractHandler {
    private final Bus bus;
    private final String portId;

    public CoreHandler(String portId, Bus bus) {
        super();
        this.portId = portId;
        this.bus = bus;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse) throws IOException, ServletException {

        DataPacket dp = this.bus.send(this.portId, new DataPacket(httpServletRequest.getParameter("m")));

        httpServletResponse.setContentType("text/html;charset=utf-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        httpServletResponse.getWriter().println(dp.message.rawResponse);
    }
}
