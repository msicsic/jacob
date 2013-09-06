package sk.jacob.mpu.security.dbregistry;

import sk.jacob.appcommon.accessor.SECURITY;
import sk.jacob.engine.handler.TokenTypes;
import sk.jacob.appcommon.accessor.CONNECTION;
import sk.jacob.mpu.security.dbregistry.model.SecurityModel;
import sk.jacob.mpu.security.dbregistry.model.Users;
import sk.jacob.sql.dml.SqlClause;
import sk.jacob.sql.engine.Connection;
import sk.jacob.sql.engine.JacobResultSet;
import sk.jacob.appcommon.types.ExecutionContext;
import sk.jacob.appcommon.types.Principal;
import sk.jacob.appcommon.types.Return;
import sk.jacob.appcommon.types.Token;

import static sk.jacob.sql.dml.DML.select;
import static sk.jacob.sql.dml.Op.and;
import static sk.jacob.sql.dml.Op.eq;
import static sk.jacob.common.util.Security.md5String;

public class FlyBy {
    private static class FlyByToken extends Token {
        public String value;
    }

    @TokenTypes(type="security.flyby.token",
           token=FlyByToken.class)
    public static ExecutionContext flyByToken(ExecutionContext ec) throws Exception {
        FlyByToken token = (FlyByToken)SECURITY.TOKEN.getFrom(ec);

        Users users = SecurityModel.INSTANCE.table(Users.class);
        SqlClause s = select(users.login, users.username, users.admin)
                .from(users)
                .where(eq(users.token, token.value));
        Connection conn = CONNECTION.CURRENT.getFrom(ec);
        JacobResultSet rs = (JacobResultSet)conn.execute(s);

        if(rs.next() == false) {
            return Return.EXCEPTION("security.invalid.token", ec);
        }

        String login =   rs.getBoolean(users.admin)
                       ? "ADMIN"
                       : rs.getString(users.login);
        String username = rs.getString(users.username);
        SECURITY.PRINCIPAL.storeValue(new Principal(login, username), ec);

        return ec;
    }

    private static class FlyByLoginPassword extends Token {
        public String login;
        public String password;
    }

    @TokenTypes(type="security.flyby.login.password",
           token=FlyByLoginPassword.class)
    public static ExecutionContext flyByLoginPassword(ExecutionContext ec) throws Exception {
        FlyByLoginPassword token = (FlyByLoginPassword)SECURITY.TOKEN.getFrom(ec);

        Users users = SecurityModel.INSTANCE.table(Users.class);
        SqlClause s = select(users.login, users.username, users.admin)
                .from(users)
                .where(and(eq(users.login, token.login),
                           eq(users.md5pwd, md5String(token.password))));
        Connection conn = CONNECTION.CURRENT.getFrom(ec);
        JacobResultSet rs = (JacobResultSet)conn.execute(s);

        if(rs.next() == false) {
            return Return.ERROR("security.invalid.login.password", ec);
        }

        String login = rs.getBoolean(users.admin) ? "ADMIN"
                                                  : rs.getString(users.login);
        String username = rs.getString(users.username);
        SECURITY.PRINCIPAL.storeValue(new Principal(login, username), ec);

        return ec;
    }
}
