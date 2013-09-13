package sk.jacob.mpu.security.dbregistry;

import sk.jacob.appcommon.accessor.SECURITY;
import sk.jacob.appcommon.annotation.Resource;
import sk.jacob.appcommon.types.*;
import sk.jacob.engine.handler.TokenTypes;
import sk.jacob.mpu.security.dbregistry.model.SecurityModel;
import sk.jacob.mpu.security.dbregistry.model.Users;
import sk.jacob.sql.dml.SqlClause;
import sk.jacob.sql.engine.Connection;
import sk.jacob.sql.engine.JacobResultSet;

import static sk.jacob.sql.dml.DML.select;
import static sk.jacob.sql.dml.Op.and;
import static sk.jacob.sql.dml.Op.eq;
import static sk.jacob.common.util.Security.md5String;

public class FlyBy {
    private static class FlyByToken extends Token {
        public String value;
    }

    @TokenTypes(type="security.flyby.token")
    public static void flyByToken(
            FlyByToken token,
            @Resource(location = "/Security/DB/Connection")Connection conn,
            @Resource(location="/ExecutionContext")ExecutionContext ec
    ) throws Exception {
        Users users = SecurityModel.INSTANCE.table(Users.class);
        SqlClause s = select(users.login, users.username, users.admin)
                .from(users)
                .where(eq(users.token, token.value));
        JacobResultSet rs = (JacobResultSet)conn.execute(s);
        ERROR.ifFalse(rs.next()).raise("security.invalid.token");
        String login =   rs.getBoolean(users.admin)
                       ? "ADMIN"
                       : rs.getString(users.login);
        String username = rs.getString(users.username);
        SECURITY.PRINCIPAL.set(new Principal(login, username), ec);
    }

    private static class FlyByLoginPassword extends Token {
        public String login;
        public String password;
    }

    @TokenTypes(type="security.flyby.login.password")
    public static void flyByLoginPassword(
            FlyByLoginPassword token,
            @Resource(location = "/Security/DB/Connection")Connection conn,
            @Resource(location="/ExecutionContext")ExecutionContext ec
    ) throws Exception {
        Users users = SecurityModel.INSTANCE.table(Users.class);
        SqlClause s = select(users.login, users.username, users.admin)
                .from(users)
                .where(and(eq(users.login, token.login),
                           eq(users.md5pwd, md5String(token.password))));
        JacobResultSet rs = (JacobResultSet)conn.execute(s);
        ERROR.ifFalse(rs.next()).raise("security.invalid.login.password");

        String login = rs.getBoolean(users.admin) ? "ADMIN"
                                                  : rs.getString(users.login);
        String username = rs.getString(users.username);
        SECURITY.PRINCIPAL.set(new Principal(login, username), ec);
    }
}
