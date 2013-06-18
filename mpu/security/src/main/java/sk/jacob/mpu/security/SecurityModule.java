package sk.jacob.mpu.security;

import sk.jacob.engine.Module;
import sk.jacob.engine.handler.HandlerInspector;
import sk.jacob.engine.handler.Token;
import sk.jacob.engine.types.DataPacket;
import sk.jacob.mpu.security.dbregistry.AuthenticateLoginPassword;

import java.util.Arrays;

public class SecurityModule implements Module {
    private static final Class[] HANDLERS = {AuthenticateLoginPassword.class};
    private final HandlerInspector<Token> handlerInspector;

    public SecurityModule() {
        handlerInspector = new SecurityHandlerInspector(Arrays.asList(HANDLERS));
    }

    @Override
    public DataPacket handle(DataPacket dataPacket) {
        return handlerInspector.handle(dataPacket);
    }
}

