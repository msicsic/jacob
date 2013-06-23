package sk.jacob.mpu.context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sk.jacob.engine.handler.HandlerInspector;
import sk.jacob.engine.handler.Message;
import sk.jacob.engine.types.DataPacket;

//TODO asi bude nejaky genericky pre vsetky moduli...
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
        String type = null;

        JsonObject jsonRequest = dataPacket.message.jsonRequest;
        JsonObject reqh = jsonRequest.get("reqh").getAsJsonObject();

        if (reqh.has("type")) {
            type = reqh.get("type").getAsString();
        }

        return type;
    }

    @Override
    protected void updateRequestData(DataPacket dataPacket, Annotation annotation) {
        try {
            Message message = (Message) annotation;
            dataPacket.message.request = new Gson().fromJson(dataPacket.message.jsonRequest, message.reqd());
            dataPacket.message.response = message.resd().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ContextHandleInspector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected DataPacket serializeResponse(DataPacket dataPacket) {
        Gson g = new Gson();
        dataPacket.message.rawResponse = g.toJson(dataPacket.message.response);
        return dataPacket;
    }
}
