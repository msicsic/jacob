package sk.jacob.mpu.security.dbregistry;

import sk.jacob.common.SECURITY;
import sk.jacob.engine.handler.Token;
import sk.jacob.sql.dml.DMLClause;
import sk.jacob.sql.dml.SqlClause;
import sk.jacob.sql.engine.Connection;
import sk.jacob.types.DataPacket;
import sk.jacob.types.ResponseDataType;
import sk.jacob.types.Return;
import sk.jacob.types.TokenType;

import java.sql.ResultSet;

import static sk.jacob.sql.dml.DML.cv;
import static sk.jacob.sql.dml.DML.select;
import static sk.jacob.sql.dml.DML.update;
import static sk.jacob.sql.dml.Op.and;
import static sk.jacob.sql.dml.Op.eq;
import static sk.jacob.util.Security.uniqueToken;
import static sk.jacob.util.Security.md5String;

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
        AuthLogPassToken token = (AuthLogPassToken) SECURITY.TOKEN.get(dataPacket);
        SqlClause s = select("login", "username", "admin")
                      .from("users")
                      .where(and(eq("login", token.login),
                                 eq("md5pwd", md5String(token.password))));
        Connection conn = (Connection) SECURITY.CONNECTION.get(dataPacket);
        ResultSet rs = (ResultSet)conn.execute(s);
        if(rs.next() == Boolean.FALSE) {
            return Return.EXCEPTION("security.invalid.login.password", dataPacket);
        }

        String generatedToken = uniqueToken();
        conn.execute(update("users").set(cv("token", generatedToken)).where(eq("login", token.login)));
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
        InvalidateToken token = (InvalidateToken) SECURITY.TOKEN.get(dataPacket);
        DMLClause s = update("users")
                         .set(cv("token", null))
                         .where(eq("token", token.value));
        Connection conn = (Connection) SECURITY.CONNECTION.get(dataPacket);
        conn.execute(s);
        return Return.EMPTY_RESPONSE(dataPacket);
    }
}
