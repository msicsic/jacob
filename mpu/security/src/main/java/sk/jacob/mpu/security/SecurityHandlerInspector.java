package sk.jacob.mpu.security;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import sk.jacob.engine.handler.HandlerInspector;
import sk.jacob.engine.handler.Token;
import sk.jacob.engine.types.DataPacket;

import java.lang.annotation.Annotation;
import java.util.List;

public class SecurityHandlerInspector extends HandlerInspector<Token> {

    public SecurityHandlerInspector(List<Class> messageHandlers) {
        super(Token.class, messageHandlers);
    }

    @Override
    protected String getHandlerKey(Token annotation) {
        return annotation.type();
    }

    @Override
    protected String getMessageType(DataPacket dataPacket) {
        JsonObject securityElement = getSecurityElement(dataPacket);
        return securityElement.get("type").getAsString();
    }

    @Override
    protected void deserializeMessageElement(DataPacket dataPacket, Annotation annotation) {
        JsonObject securityElement = getSecurityElement(dataPacket);
        Token token = (Token)annotation;
        dataPacket.security.token = new Gson().fromJson(securityElement, token.token());
    }

    private static JsonObject getSecurityElement(DataPacket dataPacket) {
        JsonObject jsonRequest = dataPacket.message.jsonRequest;
        JsonObject reqh = jsonRequest.get("reqh").getAsJsonObject();
        return reqh.get("security").getAsJsonObject();
    }
}
