package sk.jacob.mpu.security;

import com.google.gson.JsonObject;
import sk.jacob.appcommon.accessor.COMMON;
import sk.jacob.appcommon.accessor.CONFIG;
import sk.jacob.appcommon.accessor.SECURITY;
import sk.jacob.appcommon.types.Token;
import sk.jacob.engine.ApplicationModule;
import sk.jacob.mpu.security.dbregistry.Init;
import sk.jacob.mpu.security.dbregistry.model.SecurityModel;
import sk.jacob.mpu.security.dbregistry.model.Users;
import sk.jacob.sql.Metadata;
import sk.jacob.sql.dml.SqlClause;
import sk.jacob.sql.engine.Connection;
import sk.jacob.sql.engine.DbEngine;
import sk.jacob.appcommon.types.ExecutionContext;
import sk.jacob.appcommon.types.Return;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static sk.jacob.sql.dml.DML.*;
import static sk.jacob.sql.dml.Op.eq;
import static sk.jacob.common.util.Log.logger;

public class SecurityApplicationModule extends ApplicationModule<Token> {
    private final DbEngine dbEngine;
    private final Metadata MODEL = SecurityModel.INSTANCE.METADATA;

    public SecurityApplicationModule(Properties config) {
        super(Token.class, handlerClasses());
        this.dbEngine = new DbEngine(CONFIG.SECURITY_URL.get(config),
                                     CONFIG.SECURITY_USERNAME.get(config),
                                     CONFIG.SECURITY_PASSWORD.get(config));
        this.initDatabase(CONFIG.ADMIN_LOGIN.get(config),
                          CONFIG.ADMIN_PASSWORD.get(config));
    }

    private static List<Class> handlerClasses() {
        return Arrays.asList(Init.HANDLERS);
    }

    @Override
    public ExecutionContext onRequest(ExecutionContext ec) {
        Connection conn = this.dbEngine.getConnection();
        SECURITY.CONNECTION.set(conn, ec);
        try {
            conn.txBegin();
            ec = super.process(ec);
            conn.txCommit();
        } catch (Exception e){
            conn.txRollback();
            String errorCode = "security.general.token.exception";
            logger(this).error(errorCode, e);
            ec = Return.EXCEPTION(errorCode, e, ec);
        } finally {
            conn.close();
        }
        return ec;
    }

    private void initDatabase(String adminLogin, String adminMd5Pwd) {
        SecurityModel.INSTANCE.createAll(this.dbEngine);
        initializeAdmin(adminLogin, adminMd5Pwd);
    }

    private void initializeAdmin(String adminLogin, String adminMd5Pwd) {
        Users users = MODEL.table(Users.class);
        SqlClause deleteDMLClause = delete(users)
                .where(eq(users.admin, true));
        SqlClause insertDMLClause = insert(users)
                .values(cv(users.login, adminLogin),
                        cv(users.username, "Administrator"),
                        cv(users.admin, true),
                        cv(users.md5pwd, adminMd5Pwd));
        Connection conn = this.dbEngine.getConnection();
        conn.txBegin();
        conn.execute(deleteDMLClause);
        conn.execute(insertDMLClause);
        conn.txCommit();
        conn.close();
    }

    @Override
    protected String getMessageType(ExecutionContext ec) {
        JsonObject securityElement = getSecurityElement(ec);
        return securityElement.get("type").getAsString();
    }

    @Override
    protected void processPayload(ExecutionContext ec, Class<Token> payloadClass) {
        JsonObject securityElement = getSecurityElement(ec);
        SECURITY.TOKEN.set(GSON.fromJson(securityElement, payloadClass), ec);
    }

    private static JsonObject getSecurityElement(ExecutionContext ec) {
        JsonObject jsonRequest = COMMON.MESSAGE.getFrom(ec).jsonRequest;
        JsonObject reqh = jsonRequest.get("reqh").getAsJsonObject();
        return reqh.get("security").getAsJsonObject();
    }
}

