package sk.jacob.mpu.security.dbregistry;

import sk.jacob.common.SECURITY;
import sk.jacob.engine.handler.Token;
import sk.jacob.types.DataPacket;
import sk.jacob.types.Principal;
import sk.jacob.types.Return;
import sk.jacob.types.TokenType;
import sk.jacob.sql.dml.DMLStatement;
import sk.jacob.sql.engine.ExecutionContext;

import java.sql.ResultSet;

import static sk.jacob.sql.dml.DML.select;
import static sk.jacob.sql.dml.Op.and;
import static sk.jacob.sql.dml.Op.eq;
import static sk.jacob.util.Crypto.md5String;

public class FlyBy {
    private static class FlyByToken extends TokenType {
        public String value;
    }

    @Token(type="security.flyby.token",
           token=FlyByToken.class)
    public static DataPacket flyByToken(DataPacket dataPacket) throws Exception {
        FlyByToken token = (FlyByToken) SECURITY.getToken(dataPacket);
        DMLStatement s = select("login", "username", "admin")
                         .from("users")
                         .where(eq("token", token.value));
        ExecutionContext ectx = (ExecutionContext) SECURITY.EXECUTION_CTX.get(dataPacket);
        ResultSet rs = (ResultSet)ectx.execute(s);
        if(rs.next() == Boolean.FALSE) {
            return Return.EXCEPTION("security.invalid.token", dataPacket);
        }
        String login = rs.getBoolean("admin") ? "ADMIN" : rs.getString("login");
        String username = rs.getString("username");
        SECURITY.setPrincipal(dataPacket, new Principal(login, username));
        return dataPacket;
    }

    private static class FlyByLoginPassword extends TokenType {
        public String login;
        public String password;
    }

    @Token(type="security.flyby.login.password",
           token=FlyByLoginPassword.class)
    public static DataPacket flyByLoginPassword(DataPacket dataPacket) throws Exception {
        FlyByLoginPassword token = (FlyByLoginPassword)  SECURITY.getToken(dataPacket);
        DMLStatement s = select("login", "username", "admin")
                         .from("users")
                         .where(and(eq("login", token.login),
                                 eq("md5pwd", md5String(token.password))));
        ExecutionContext ectx = (ExecutionContext) SECURITY.EXECUTION_CTX.get(dataPacket);
        ResultSet rs = (ResultSet)ectx.execute(s);
        if(rs.next() == Boolean.FALSE) {
            return Return.EXCEPTION("security.invalid.login.password", dataPacket);
        }

        String login = rs.getBoolean("admin") ? "ADMIN" : rs.getString("login");
        String username = rs.getString("username");
        SECURITY.setPrincipal(dataPacket, new Principal(login, username));
        return dataPacket;
    }
}
