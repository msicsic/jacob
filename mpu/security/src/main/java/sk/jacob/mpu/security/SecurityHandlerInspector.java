package sk.jacob.mpu.security;

import com.google.gson.*;
import sk.jacob.engine.handler.HandlerInspector;
import sk.jacob.engine.handler.Token;
import sk.jacob.engine.types.DataPacket;
import sk.jacob.engine.types.TokenType;
import sk.jacob.mpu.security.dbregistry.AuthenticateLoginPassword;

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
    protected void updateRequestData(DataPacket dataPacket, Annotation annotation) {
        JsonObject securityElement = getSecurityElement(dataPacket);
        Token token = (Token)annotation;
        dataPacket.security.token = new Gson().fromJson(securityElement, token.token());
    }

    @Override
    protected DataPacket serializeResponse(DataPacket dataPacket) {
        Gson g = new Gson();
        dataPacket.message.rawResponse = g.toJson(dataPacket.message.response);
        return dataPacket;
    }

    private JsonObject getSecurityElement(DataPacket dataPacket) {
        JsonObject jsonRequest = dataPacket.message.jsonRequest;
        JsonObject reqh = jsonRequest.get("reqh").getAsJsonObject();
        return reqh.get("security").getAsJsonObject();
    }
}
