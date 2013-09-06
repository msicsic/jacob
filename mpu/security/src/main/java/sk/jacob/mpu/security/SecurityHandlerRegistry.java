package sk.jacob.mpu.security;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import sk.jacob.appcommon.accessor.COMMON;
import sk.jacob.appcommon.accessor.SECURITY;
import sk.jacob.engine.handler.HandlerRegistry;
import sk.jacob.engine.handler.TokenTypes;
import sk.jacob.appcommon.types.ExecutionContext;

import java.lang.annotation.Annotation;
import java.util.List;

public class SecurityHandlerRegistry extends HandlerRegistry<TokenTypes> {
    private static final Gson GSON = new Gson();

    public SecurityHandlerRegistry(List<Class> messageHandlers) {
        super(TokenTypes.class, messageHandlers);
    }

    @Override
    protected String getHandlerKey(TokenTypes annotation) {
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
        TokenTypes tokenTypes = (TokenTypes)annotation;
        SECURITY.TOKEN.storeValue(GSON.fromJson(securityElement, tokenTypes.token()), ec);
    }

    private static JsonObject getSecurityElement(ExecutionContext ec) {
        JsonObject jsonRequest = COMMON.getMessage(ec).jsonRequest;
        JsonObject reqh = jsonRequest.get("reqh").getAsJsonObject();
        return reqh.get("security").getAsJsonObject();
    }
}
