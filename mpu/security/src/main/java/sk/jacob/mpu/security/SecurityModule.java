package sk.jacob.mpu.security;

import sk.jacob.common.SECURITY;
import sk.jacob.engine.Module;
import sk.jacob.engine.handler.HandlerInspector;
import sk.jacob.engine.handler.Token;
import sk.jacob.mpu.security.dbregistry.Model.SecurityModel;
import sk.jacob.mpu.security.dbregistry.Model.Users;
import sk.jacob.sql.Metadata;
import sk.jacob.sql.dml.SqlClause;
import sk.jacob.sql.engine.Connection;
import sk.jacob.types.DataPacket;
import sk.jacob.mpu.security.dbregistry.Init;
import sk.jacob.sql.engine.DbEngine;
import sk.jacob.types.Return;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static sk.jacob.sql.dml.DML.*;
import static sk.jacob.sql.dml.DML.cv;
import static sk.jacob.sql.dml.Op.eq;
import static sk.jacob.util.Log.logger;

public class SecurityModule implements Module {
    private static final List<Class> HANDLERS = new ArrayList<>();
    private final HandlerInspector<Token> handlerInspector;
    private final DbEngine dbEngine;
    private final Metadata MODEL = SecurityModel.metadata();

    static {
        HANDLERS.addAll(Arrays.asList(Init.HANDLERS));
    }

    public SecurityModule(Properties config) {
        this.handlerInspector = new SecurityHandlerInspector(HANDLERS);
        this.dbEngine = new DbEngine(config.getProperty("security.url"),
                                     config.getProperty("security.username"),
                                     config.getProperty("security.password"));
        this.initDatabase(config.getProperty("admin.login"),
                          config.getProperty("admin.md5pwd"));
    }

    @Override
    public DataPacket handle(DataPacket dataPacket) {
        Connection conn = this.dbEngine.getConnection();
        SECURITY.CONNECTION.set(dataPacket, conn);
        try {
            conn.txBegin();
            dataPacket = this.handlerInspector.process(dataPacket);
            conn.txCommit();
        } catch (Exception e){
            String errorCode = "security.general.token.exception";
            logger(this).error(errorCode, e);
            conn.txRollback();
            dataPacket = Return.EXCEPTION(errorCode, e, dataPacket);
        } finally {
            conn.close();
        }
        return dataPacket;
    }

    private void initDatabase(String adminLogin, String adminMd5Pwd) {
        SecurityModel.createAll(this.dbEngine);
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

