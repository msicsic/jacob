package sk.jacob.mpu.security.dbregistry;

import sk.jacob.engine.handler.Token;
import sk.jacob.engine.types.DataPacket;
import sk.jacob.engine.types.TokenType;
import sk.jacob.sql.dml.DMLStatement;
import sk.jacob.sql.engine.ExecutionContext;

import java.sql.ResultSet;

import static sk.jacob.sql.dml.DML.select;
import static sk.jacob.sql.dml.Function.count;
import static sk.jacob.sql.dml.Op.eq;

public class FlyBy {
    private static class FlyByToken extends TokenType {
        public String value;
    }

    @Token(type="security.flyby.token",
           token=FlyByToken.class)
    public static DataPacket flyByToken(DataPacket dataPacket) throws Exception {
        FlyByToken token = (FlyByToken)dataPacket.security.token;
        DMLStatement s =
                select(count("*"))
                        .from("users")
                        .where(eq("token", token.value));
        ExecutionContext ectx = (ExecutionContext)SEC_CTX.EXECUTION_CTX.get(dataPacket);
        ResultSet rs = (ResultSet)ectx.execute(s);
        rs.next();
        if(rs.getLong(1) != 1) {
            // return soft exception
        }
        //dataPacket.security.username;
        return dataPacket;
    }

    private static class FlyByLoginPassword extends TokenType {
        public String login;
        public String password;
    }

    @Token(type="security.flyby.login.password",
           token=FlyByLoginPassword.class)
    public static DataPacket flyByLoginPassword(DataPacket dataPacket) throws Exception {
        return dataPacket;
    }
}
