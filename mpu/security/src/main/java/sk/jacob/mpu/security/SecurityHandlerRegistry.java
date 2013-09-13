package sk.jacob.mpu.security;

import com.google.gson.JsonObject;
import sk.jacob.appcommon.accessor.COMMON;
import sk.jacob.appcommon.accessor.SECURITY;
import sk.jacob.appcommon.types.Token;
import sk.jacob.engine.handler.HandlerRegistry;
import sk.jacob.engine.handler.TokenTypes;
import sk.jacob.appcommon.types.ExecutionContext;
import java.util.List;

public class SecurityHandlerRegistry extends HandlerRegistry<TokenTypes, Token> {
    public SecurityHandlerRegistry(List<Class> messageHandlers) {
        super(TokenTypes.class, messageHandlers);
    }

    @Override
    protected String getHandlerType(TokenTypes annotation) {
        return annotation.type();
    }

    @Override
    protected String getMessageType(ExecutionContext ec) {
        JsonObject securityElement = getSecurityElement(ec);
        return securityElement.get("type").getAsString();
    }

    @Override
    protected void processRequestClass(ExecutionContext ec, Class<Token> payloadClass) {
        JsonObject securityElement = getSecurityElement(ec);
        SECURITY.TOKEN.set(GSON.fromJson(securityElement, payloadClass), ec);
    }

    private static JsonObject getSecurityElement(ExecutionContext ec) {
        JsonObject jsonRequest = COMMON.MESSAGE.getFrom(ec).jsonRequest;
        JsonObject reqh = jsonRequest.get("reqh").getAsJsonObject();
        return reqh.get("security").getAsJsonObject();
    }
}
