package sk.jacob.mpu.security.dbregistry;

import sk.jacob.appcommon.accessor.SECURITY;
import sk.jacob.appcommon.annotation.Resource;
import sk.jacob.appcommon.types.*;
import sk.jacob.engine.handler.TokenTypes;
import sk.jacob.mpu.security.dbregistry.model.SecurityModel;
import sk.jacob.mpu.security.dbregistry.model.Users;
import sk.jacob.sql.dml.DMLClause;
import sk.jacob.sql.dml.SqlClause;
import sk.jacob.sql.engine.Connection;
import sk.jacob.sql.engine.JacobResultSet;


import java.lang.Exception;

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

    @TokenTypes(type="security.authenticate.login.password",
                token=AuthLogPassToken.class,
                resd=AuthLogPassResd.class)
    public static ExecutionContext authenticateLoginPassword(
            AuthLogPassToken token,
            @Resource(location="/ExecutionContext")ExecutionContext ec,
            @Resource(location = "/Security/DB/Connection")Connection conn
    ) throws Exception {
        Users users = SecurityModel.INSTANCE.table(Users.class);
        SqlClause s = select(users.login, users.username, users.admin)
                .from(users)
                .where(and(eq(users.login, token.login),
                        eq(users.md5pwd, md5String(token.password))));
        JacobResultSet rs = (JacobResultSet)conn.execute(s);
        ERROR.ifFalse(rs.next()).raise("security.invalid.login.password");

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

        return Return.RESPONSE(resd, ec);
    }

    private static class InvalidateToken extends Token {
        public String value;
    }

    @TokenTypes(type="security.invalidate.token",
           token=InvalidateToken.class)
    public static ExecutionContext invalidateToken(ExecutionContext ec) throws Exception {
        InvalidateToken token = (InvalidateToken) SECURITY.TOKEN.getFrom(ec);

        Users users = SecurityModel.INSTANCE.table(Users.class);
        DMLClause s = update(users)
                .set(cv(users.token, null))
                .where(eq(users.token, token.value));
        Connection conn = SECURITY.CONNECTION.getFrom(ec);
        conn.execute(s);

        return Return.EMPTY_RESPONSE(ec);
    }
}
