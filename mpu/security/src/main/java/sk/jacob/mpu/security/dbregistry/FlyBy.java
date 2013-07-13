package sk.jacob.mpu.security.dbregistry;

import sk.jacob.common.SECURITY;
import sk.jacob.engine.handler.Token;
import sk.jacob.mpu.security.dbregistry.model.SecurityModel;
import sk.jacob.mpu.security.dbregistry.model.Users;
import sk.jacob.sql.dml.SqlClause;
import sk.jacob.sql.engine.Connection;
import sk.jacob.types.DataPacket;
import sk.jacob.types.Principal;
import sk.jacob.types.Return;
import sk.jacob.types.TokenType;

import java.sql.ResultSet;

import static sk.jacob.sql.dml.DML.select;
import static sk.jacob.sql.dml.Op.and;
import static sk.jacob.sql.dml.Op.eq;
import static sk.jacob.util.Security.md5String;

public class FlyBy {
    private static class FlyByToken extends TokenType {
        public String value;
    }

    @Token(type="security.flyby.token",
           token=FlyByToken.class)
    public static DataPacket flyByToken(DataPacket dataPacket) throws Exception {
        FlyByToken token = (FlyByToken) SECURITY.TOKEN.get(dataPacket);

        Users users = SecurityModel.table(Users.class);
        SqlClause s = select(users.login, users.username, users.admin)
                .from(users)
                .where(eq(users.token, token.value));
        Connection conn = (Connection) SECURITY.CONNECTION.get(dataPacket);
        ResultSet rs = (ResultSet)conn.execute(s);

        if(rs.next() == false) {
            return Return.EXCEPTION("security.invalid.token", dataPacket);
        }

        String login = rs.getBoolean(users.admin.name) ? "ADMIN"
                                                       : rs.getString(users.login.name);
        String username = rs.getString(users.username.name);
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
        FlyByLoginPassword token = (FlyByLoginPassword)  SECURITY.TOKEN.get(dataPacket);

        Users users = SecurityModel.table(Users.class);
        SqlClause s = select(users.login, users.username, users.admin)
                .from(users)
                .where(and(eq(users.login, token.login),
                           eq(users.md5pwd, md5String(token.password))));
        Connection conn = (Connection) SECURITY.CONNECTION.get(dataPacket);
        ResultSet rs = (ResultSet)conn.execute(s);

        if(rs.next() == false) {
            return Return.EXCEPTION("security.invalid.login.password", dataPacket);
        }

        String login = rs.getBoolean(users.admin.name) ? "ADMIN"
                                                       : rs.getString(users.login.name);
        String username = rs.getString(users.username.name);
        SECURITY.setPrincipal(dataPacket, new Principal(login, username));

        return dataPacket;
    }
}
