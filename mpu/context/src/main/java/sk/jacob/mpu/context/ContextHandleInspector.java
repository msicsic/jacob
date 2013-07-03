package sk.jacob.mpu.context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.lang.annotation.Annotation;
import java.util.List;
import sk.jacob.engine.handler.HandlerInspector;
import sk.jacob.engine.handler.Message;
import sk.jacob.types.DataPacket;
import sk.jacob.types.RequestType;

// FIXME:
public class ContextHandleInspector extends HandlerInspector<Message> {
    public ContextHandleInspector(List<Class> messageHandlers) {
        super(Message.class, messageHandlers);
    }

    @Override
    protected String getHandlerKey(Message annotation) {
        return annotation.type();
    }

    @Override
    protected String getMessageType(DataPacket dataPacket) {
        String type = dataPacket.message.request.reqh.type;
        return type;
    }

    @Override
    protected void deserializeMessageElement(DataPacket dataPacket, Annotation annotation) {
        try {
            Message message = (Message)annotation;
            dataPacket.message.request.reqd = new Gson().fromJson(dataPacket.message.jsonRequest, message.reqd());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}