package sk.jacob.mpu.security;

import com.google.gson.*;
import sk.jacob.engine.handler.HandlerInspector;
import sk.jacob.engine.handler.Token;
import sk.jacob.engine.types.DataPacket;
import sk.jacob.mpu.security.dbregistry.AuthenticateLoginPassword;

import java.util.List;

public class SecurityHandlerInspector extends HandlerInspector<Token> {

    public SecurityHandlerInspector(List<Class> messageHandlers) {
        super(Token.class, messageHandlers);
    }

    @Override
    public DataPacket handle(DataPacket dataPacket) {
        String rawRequest = dataPacket.message.rawRequest;
        String handlerKey = this.typeResolver.fromJson(rawRequest, String.class);
        AuthenticateLoginPassword.AuthLogPassReq request =
                this.typeResolver.fromJson(rawRequest, AuthenticateLoginPassword.AuthLogPassReq.class);
        return invokeMethod(handlerKey, dataPacket);
    }

    @Override
    public String getHandlerKey(Token annotation) {
        return annotation.type();
    }

    @Override
    public String getMessageType(JsonElement jsonRequest) {
        JsonObject json = jsonRequest.getAsJsonObject();
        JsonObject reqh = json.get("reqh").getAsJsonObject();
        JsonObject securityElement = reqh.get("security").getAsJsonObject();
        return securityElement.get("type").getAsString();
    }
}
