package sk.jacob.connector.http;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import sk.jacob.appcommon.accessor.COMMON;
import sk.jacob.appcommon.types.Message;
import sk.jacob.engine.InPort;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CoreHandler extends AbstractHandler {
    private final InPort inPort;

    public CoreHandler(InPort inPort) {
        super();
        this.inPort = inPort;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse) throws IOException, ServletException {

        Message message = this.inPort.onRequest(COMMON.createMessage(httpServletRequest.getParameter("m")));

        httpServletResponse.setContentType("text/html;charset=utf-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        httpServletResponse.getWriter().println(message.rawResponse);
    }
}
