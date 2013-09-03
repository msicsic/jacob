package sk.jacob.mpu.security;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import sk.jacob.accessor.COMMON;
import sk.jacob.accessor.SECURITY;
import sk.jacob.engine.handler.HandlerRegistry;
import sk.jacob.engine.handler.Token;
import sk.jacob.types.ExecutionContext;

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
    protected String getMessageType(ExecutionContext ec) {
        JsonObject securityElement = getSecurityElement(ec);
        return securityElement.get("type").getAsString();
    }

    @Override
    protected void deserializeMessageElement(ExecutionContext ec, Annotation annotation) {
        JsonObject securityElement = getSecurityElement(ec);
        Token token = (Token)annotation;
        SECURITY.TOKEN.set(ec, GSON.fromJson(securityElement, token.token()));
    }

    private static JsonObject getSecurityElement(ExecutionContext ec) {
        JsonObject jsonRequest = COMMON.getMessage(ec).jsonRequest;
        JsonObject reqh = jsonRequest.get("reqh").getAsJsonObject();
        return reqh.get("security").getAsJsonObject();
    }
}
