package sk.jacob.mpu.security.dbregistry;

import sk.jacob.engine.handler.Token;
import sk.jacob.engine.types.DataPacket;
import sk.jacob.engine.types.ResponseType;
import sk.jacob.engine.types.TokenType;

public class AuthenticateLoginPassword {
    public static class AuthLogPassReq extends TokenType {
        public String login;
        public String password;
    }

    public static class AuthLogPassRes extends ResponseType {
        public String token;

        public static class principal{
            public String login;
            public String username;
        }
    }

    @Token(type="security.authenticate.login.password",
           version="1.0",
           token=AuthLogPassReq.class,
           resd=AuthLogPassRes.class)
    public DataPacket handle(DataPacket dataPacket) {
        return dataPacket;
    }
}
