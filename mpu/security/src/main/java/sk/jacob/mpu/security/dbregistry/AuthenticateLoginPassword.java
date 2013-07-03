package sk.jacob.mpu.security.dbregistry;

import sk.jacob.common.SECURITY;
import sk.jacob.engine.handler.Token;
import sk.jacob.types.DataPacket;
import sk.jacob.types.ResponseDataType;
import sk.jacob.types.Return;
import sk.jacob.types.TokenType;
import sk.jacob.mpu.security.TokenGenerator;
import sk.jacob.sql.dml.DMLStatement;
import sk.jacob.sql.engine.ExecutionContext;

import java.sql.ResultSet;

import static sk.jacob.sql.dml.DML.cv;
import static sk.jacob.sql.dml.DML.select;
import static sk.jacob.sql.dml.DML.update;
import static sk.jacob.sql.dml.Op.and;
import static sk.jacob.sql.dml.Op.eq;
import static sk.jacob.util.Crypto.md5String;

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
    public static DataPacket authenticateLoginPassword(DataPacket dataPacket) throws Exception {
        AuthLogPassToken token = (AuthLogPassToken) SECURITY.getToken(dataPacket);
        DMLStatement s = select("login", "username", "admin")
                         .from("users")
                         .where(and(eq("login", token.login),
                                 eq("md5pwd", md5String(token.password))));
        ExecutionContext ectx = (ExecutionContext) SECURITY.EXECUTION_CTX.get(dataPacket);
        ResultSet rs = (ResultSet)ectx.execute(s);
        if(rs.next() == Boolean.FALSE) {
            return Return.EXCEPTION("security.invalid.login.password", dataPacket);
        }
        String generatedToken = TokenGenerator.getToken();
        ectx.execute(update("users").set(cv("token", generatedToken)).where(eq("login", token.login)));
        AuthLogPassResd resd = new AuthLogPassResd();
        resd.token = generatedToken;
        resd.principal.login = rs.getBoolean("admin") ? "ADMIN" : rs.getString("login");
        resd.principal.username = rs.getString("username");
        return Return.RESPONSE(resd, dataPacket);
    }

    private static class InvalidateToken extends TokenType {
        public String value;
    }

    @Token(type="security.invalidate.token",
           token=InvalidateToken.class)
    public static DataPacket invalidateToken(DataPacket dataPacket) throws Exception {
        InvalidateToken token = (InvalidateToken) SECURITY.getToken(dataPacket);
        DMLStatement s = update("users")
                         .set(cv("token", null))
                         .where(eq("token", token.value));
        ExecutionContext ectx = (ExecutionContext) SECURITY.EXECUTION_CTX.get(dataPacket);
        ectx.execute(s);
        return Return.EMPTY_RESPONSE(dataPacket);
    }
}
