package sk.jacob.mpu.security.dbregistry;

import sk.jacob.appcommon.accessor.SECURITY;
import sk.jacob.engine.handler.annotation.Resource;
import sk.jacob.appcommon.types.*;
import sk.jacob.engine.handler.annotation.Handler;
import sk.jacob.engine.handler.annotation.Payload;
import sk.jacob.mpu.security.dbregistry.codes.SecurityINT;
import sk.jacob.mpu.security.dbregistry.model.SecurityModel;
import sk.jacob.mpu.security.dbregistry.model.Users;
import sk.jacob.sql.dml.DMLClause;
import sk.jacob.sql.dml.SqlClause;
import sk.jacob.sql.engine.Connection;
import sk.jacob.sql.engine.JacobResultSet;

import static sk.jacob.sql.dml.DML.*;
import static sk.jacob.sql.dml.Op.and;
import static sk.jacob.sql.dml.Op.eq;
import static sk.jacob.common.util.Security.md5String;
import static sk.jacob.common.util.Security.uniqueToken;

public class AuthenticateLoginPassword {
    private static class AuthLogPassToken extends Token {
        public String login;
        public String password;
    }

    private static class AuthLogPassResd extends ResponseData {
        public static class Principal{
            public String login;
            public String username;
        }

        public String token;
        public Principal principal = new Principal();
    }

    @Handler(type="security.authenticate.login.password")
    public static AuthLogPassResd authenticateLoginPassword(
            @Payload AuthLogPassToken token,
            @Resource(location = SECURITY.CONNECTION_KEY)Connection conn
    ) throws Exception {
        Users users = SecurityModel.INSTANCE.table(Users.class);
        SqlClause s = select(users.login, users.username, users.admin)
                .from(users)
                .where(and(eq(users.login, token.login),
                        eq(users.md5pwd, md5String(token.password))));
        JacobResultSet rs = (JacobResultSet)conn.execute(s);
        Interrupt.ifFalse(rs.next()).raise(SecurityINT.INVALID_LOGIN_PASSWORD);

        String generatedToken = uniqueToken();
        SqlClause u = update(users)
                .set(cv(users.token, generatedToken))
                .where(eq(users.login, token.login));
        conn.execute(u);
        AuthLogPassResd resd = new AuthLogPassResd();
        resd.token = generatedToken;
        resd.principal.login =   rs.getBoolean(users.admin)
                               ? "ADMIN"
                               : rs.getString(users.login);
        resd.principal.username = rs.getString(users.username);
        return resd;
    }

    private static class InvalidateToken extends Token {
        public String value;
    }

    @Handler(type="security.invalidate.token")
    public static void invalidateToken(
            @Payload InvalidateToken token,
            @Resource(location = SECURITY.CONNECTION_KEY)Connection conn
    ) throws Exception {
        Users users = SecurityModel.INSTANCE.table(Users.class);
        DMLClause s = update(users)
                .set(cv(users.token, null))
                .where(eq(users.token, token.value));
        conn.execute(s);
    }
}
