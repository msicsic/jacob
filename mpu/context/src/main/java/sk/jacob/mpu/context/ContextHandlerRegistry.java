package sk.jacob.mpu.context;

import com.google.gson.Gson;
import java.lang.annotation.Annotation;
import java.util.List;

import com.google.gson.JsonObject;
import sk.jacob.common.MESSAGE;
import sk.jacob.engine.handler.HandlerRegistry;
import sk.jacob.engine.handler.Message;
import sk.jacob.types.DataPacket;
import sk.jacob.types.RequestHeaderType;
import sk.jacob.types.RequestType;

public class ContextHandlerRegistry extends HandlerRegistry<Message> {
    public ContextHandlerRegistry(List<Class> messageHandlers) {
        super(Message.class, messageHandlers);
    }

    @Override
    protected String getHandlerKey(Message annotation) {
        return annotation.type();
    }

    @Override
    protected String getMessageType(DataPacket dataPacket) {
        return MESSAGE.current(dataPacket).request.reqh.type;
    }

    @Override
    protected void deserializeMessageElement(DataPacket dataPacket, Annotation annotation) {
        Message message = (Message) annotation;
        JsonObject jsonRequest = MESSAGE.current(dataPacket).jsonRequest;
        RequestType request = new RequestType();

        request.reqh = new Gson().fromJson(jsonRequest.get("reqh"), RequestHeaderType.class);
        request.reqd = new Gson().fromJson(jsonRequest.get("reqd"), message.reqd());

        MESSAGE.current(dataPacket).request = request;
    }
}