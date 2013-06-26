package sk.jacob.mpu.security.dbregistry;

import sk.jacob.engine.handler.Token;
import sk.jacob.engine.types.DataPacket;
import sk.jacob.engine.types.ResponseDataType;
import sk.jacob.engine.types.TokenType;

public class AuthenticateLoginPassword {
    private static class AuthLogPassToken extends TokenType {
        public String login;
        public String password;
    }

    private static class AuthLogPassResd extends ResponseDataType {
        public static class Principal{
            public String login;
            public String username;
        }

        public String token;
        public Principal principal = new Principal();
    }

    @Token(type="security.authenticate.login.password",
           token=AuthLogPassToken.class,
           resd=AuthLogPassResd.class)
    public static DataPacket handle(DataPacket dataPacket) {
        AuthLogPassToken token = (AuthLogPassToken)dataPacket.security.token;
        AuthLogPassResd resd = new AuthLogPassResd();
        resd.token = "TOKEN 12345";
        resd.principal.login = "ADMIN";
        resd.principal.username = "ADMINISTRATOR";
        dataPacket.message.createResponse(resd);
        return dataPacket;
    }
}
