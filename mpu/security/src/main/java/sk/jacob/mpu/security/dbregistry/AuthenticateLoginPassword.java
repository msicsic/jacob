package sk.jacob.mpu.security.dbregistry;

import sk.jacob.engine.handler.Token;
import sk.jacob.engine.types.*;

public class AuthenticateLoginPassword {
    public static class AuthLogPassToken extends TokenType {
        public String login;
        public String password;
    }

    public static class AuthLogPassRes implements ResponseDataType {
        public String token;
        public Principal principal = new Principal();

        public static class Principal{
            public String login;
            public String username;
        }
    }

    @Token(type="security.authenticate.login.password",
           token=AuthLogPassToken.class,
           resd=AuthLogPassRes.class)
    public static DataPacket handle(DataPacket dataPacket) {
        AuthLogPassToken token = (AuthLogPassToken)dataPacket.security.token;
        AuthLogPassRes resd = new AuthLogPassRes();
        resd.token = "TOKEN 12345";
        resd.principal.login = "ADMIN";
        resd.principal.username = "ADMINISTRATOR";
        dataPacket.message.createResponse(resd);
        return dataPacket;
    }
}
