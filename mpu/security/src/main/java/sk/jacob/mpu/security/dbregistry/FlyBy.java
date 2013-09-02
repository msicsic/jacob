package sk.jacob.mpu.security.dbregistry;

import sk.jacob.common.SECURITY;
import sk.jacob.mpu.security.dbregistry.model.SecurityModel;
import sk.jacob.mpu.security.dbregistry.model.Users;
import sk.jacob.sql.dml.SqlClause;
import sk.jacob.sql.engine.Connection;
import sk.jacob.sql.engine.JacobResultSet;
import sk.jacob.types.ExecutionContext;
import sk.jacob.types.Principal;
import sk.jacob.types.Return;
import sk.jacob.types.Token;

import static sk.jacob.sql.dml.DML.select;
import static sk.jacob.sql.dml.Op.and;
import static sk.jacob.sql.dml.Op.eq;
import static sk.jacob.util.Security.md5String;

public class FlyBy {
    private static class FlyByToken extends Token {
        public String value;
    }

    @sk.jacob.engine.handler.Token(type="security.flyby.token",
           token=FlyByToken.class)
    public static ExecutionContext flyByToken(ExecutionContext executionContext) throws Exception {
        FlyByToken token = (FlyByToken) SECURITY.TOKEN.get(executionContext);

        Users users = SecurityModel.INSTANCE.table(Users.class);
        SqlClause s = select(users.login, users.username, users.admin)
                .from(users)
                .where(eq(users.token, token.value));
        Connection conn = (Connection) SECURITY.CONNECTION.get(executionContext);
        JacobResultSet rs = (JacobResultSet)conn.execute(s);

        if(rs.next() == false) {
            return Return.EXCEPTION("security.invalid.token", executionContext);
        }

        String login =   rs.getBoolean(users.admin)
                       ? "ADMIN"
                       : rs.getString(users.login);
        String username = rs.getString(users.username);
        SECURITY.setPrincipal(executionContext, new Principal(login, username));

        return executionContext;
    }

    private static class FlyByLoginPassword extends Token {
        public String login;
        public String password;
    }

    @sk.jacob.engine.handler.Token(type="security.flyby.login.password",
           token=FlyByLoginPassword.class)
    public static ExecutionContext flyByLoginPassword(ExecutionContext executionContext) throws Exception {
        FlyByLoginPassword token = (FlyByLoginPassword)  SECURITY.TOKEN.get(executionContext);

        Users users = SecurityModel.INSTANCE.table(Users.class);
        SqlClause s = select(users.login, users.username, users.admin)
                .from(users)
                .where(and(eq(users.login, token.login),
                           eq(users.md5pwd, md5String(token.password))));
        Connection conn = (Connection) SECURITY.CONNECTION.get(executionContext);
        JacobResultSet rs = (JacobResultSet)conn.execute(s);

        if(rs.next() == false) {
            return Return.ERROR("security.invalid.login.password", executionContext);
        }

        String login = rs.getBoolean(users.admin) ? "ADMIN"
                                                  : rs.getString(users.login);
        String username = rs.getString(users.username);
        SECURITY.setPrincipal(executionContext, new Principal(login, username));

        return executionContext;
    }
}
