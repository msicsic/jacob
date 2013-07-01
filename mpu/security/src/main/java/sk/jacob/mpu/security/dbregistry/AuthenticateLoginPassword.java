package sk.jacob.mpu.security.dbregistry;

import sk.jacob.engine.handler.Token;
import sk.jacob.engine.types.DataPacket;
import sk.jacob.engine.types.ResponseDataType;
import sk.jacob.engine.types.Return;
import sk.jacob.engine.types.TokenType;
import sk.jacob.mpu.security.TokenGenerator;
import sk.jacob.sql.dml.DMLStatement;
import sk.jacob.sql.engine.ExecutionContext;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;

import static sk.jacob.sql.dml.DML.select;
import static sk.jacob.sql.dml.Op.and;
import static sk.jacob.sql.dml.Op.eq;

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
    public static DataPacket handle(DataPacket dataPacket) throws Exception {
        AuthLogPassToken token = (AuthLogPassToken)dataPacket.security.token;
        DMLStatement s =
                select("login", "username", "admin")
                        .from("users")
                        .where(and(eq("login", token.login),
                                eq("md5pwd", md5String(token.password))));
        ExecutionContext ectx = (ExecutionContext)SEC_CTX.EXECUTION_CTX.get(dataPacket);
        ResultSet rs = (ResultSet)ectx.execute(s);
        if(rs.next() == Boolean.FALSE) {
            return Return.EXCEPTION("security.invalid.login.password", dataPacket);
        }

        AuthLogPassResd resd = new AuthLogPassResd();
        resd.token = TokenGenerator.getToken();
        resd.principal.login = rs.getBoolean("admin") ? "ADMIN" : rs.getString("login");
        resd.principal.username = rs.getString("username");
        return Return.OK(resd, dataPacket);
    }

    private static class InvalidateToken extends TokenType {
        public String value;
    }

    @Token(type="security.flyby.token",
            token=InvalidateToken.class)
    public static DataPacket invalidateToken(DataPacket dataPacket) throws Exception {
        return dataPacket;
    }

    private static String md5String(String inputString) {
        String md5String;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(inputString.getBytes());
            byte[] dig = md.digest();
            StringBuffer sb = new StringBuffer();

            for (int i=0; i < dig.length; i++) {
                sb.append(Integer.toString( ( dig[i] & 0xff ) + 0x100, 16).substring( 1 ));
            }
            md5String = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return md5String;
    }
}
