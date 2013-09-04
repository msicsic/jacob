package sk.jacob.mpu.security;

import sk.jacob.accessor.CONFIG;
import sk.jacob.accessor.SECURITY;
import sk.jacob.engine.Module;
import sk.jacob.engine.handler.HandlerRegistry;
import sk.jacob.engine.handler.TokenTypes;
import sk.jacob.mpu.security.dbregistry.Init;
import sk.jacob.mpu.security.dbregistry.model.SecurityModel;
import sk.jacob.mpu.security.dbregistry.model.Users;
import sk.jacob.sql.Metadata;
import sk.jacob.sql.dml.SqlClause;
import sk.jacob.sql.engine.Connection;
import sk.jacob.sql.engine.DbEngine;
import sk.jacob.types.ExecutionContext;
import sk.jacob.types.Return;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static sk.jacob.sql.dml.DML.*;
import static sk.jacob.sql.dml.Op.eq;
import static sk.jacob.util.Log.logger;

public class SecurityModule implements Module {
    private static final List<Class> HANDLERS = new ArrayList<>();
    private final HandlerRegistry<TokenTypes> handlerRegistry;
    private final DbEngine dbEngine;
    private final Metadata MODEL = SecurityModel.INSTANCE.METADATA;

    static {
        HANDLERS.addAll(Arrays.asList(Init.HANDLERS));
    }

    public SecurityModule(Properties config) {
        this.handlerRegistry = new SecurityHandlerRegistry(HANDLERS);
        this.dbEngine = new DbEngine(CONFIG.SECURITY_URL.get(config),
                                     CONFIG.SECURITY_USERNAME.get(config),
                                     CONFIG.SECURITY_PASSWORD.get(config));
        this.initDatabase(CONFIG.ADMIN_LOGIN.get(config),
                          CONFIG.ADMIN_PASSWORD.get(config));
    }

    @Override
    public ExecutionContext handle(ExecutionContext ec) {
        Connection conn = this.dbEngine.getConnection();
        SECURITY.CONNECTION.set(ec, conn);
        try {
            conn.txBegin();
            ec = this.handlerRegistry.process(ec);
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
}

