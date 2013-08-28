package sk.jacob.mpu.security;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import sk.jacob.common.MESSAGE;
import sk.jacob.common.SECURITY;
import sk.jacob.engine.handler.HandlerRegistry;
import sk.jacob.engine.handler.Token;
import sk.jacob.types.DataPacket;

import java.lang.annotation.Annotation;
import java.util.List;

public class SecurityHandlerRegistry extends HandlerRegistry<Token> {
    private static final Gson GSON = new Gson();

    public SecurityHandlerRegistry(List<Class> messageHandlers) {
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
        SECURITY.TOKEN.set(dataPacket, GSON.fromJson(securityElement, token.token()));
    }

    private static JsonObject getSecurityElement(DataPacket dataPacket) {
        JsonObject jsonRequest = MESSAGE.current(dataPacket).jsonRequest;
        JsonObject reqh = jsonRequest.get("reqh").getAsJsonObject();
        return reqh.get("security").getAsJsonObject();
    }
}
