package sk.jacob.mpu.security;

import sk.jacob.engine.Module;
import sk.jacob.engine.handler.HandlerInspector;
import sk.jacob.engine.handler.Token;
import sk.jacob.engine.types.DataPacket;
import sk.jacob.mpu.security.dbregistry.Init;
import sk.jacob.mpu.security.dbregistry.Model;
import sk.jacob.mpu.security.dbregistry.SEC_CTX;
import sk.jacob.sql.engine.DbEngine;
import sk.jacob.sql.engine.ExecutionContext;
import sk.jacob.sql.Metadata;
import sk.jacob.sql.dml.Op;
import sk.jacob.sql.dialect.Statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static sk.jacob.sql.dml.DML.*;
import static sk.jacob.sql.dml.Op.eq;

public class SecurityModule implements Module {
    private static final List<Class> HANDLERS = new ArrayList<Class>();
    private static final Metadata MODEL = Model.get();
    private final HandlerInspector<Token> handlerInspector;
    private final DbEngine dbEngine;

    static {
        HANDLERS.addAll(Arrays.asList(Init.HANDLERS));
    }

    public SecurityModule(Properties config) {
        this.handlerInspector = new SecurityHandlerInspector(HANDLERS);
        this.dbEngine =
                new DbEngine(
                        config.getProperty("security.url"),
                        config.getProperty("security.username"),
                        config.getProperty("security.password"));
        this.initDatabase(config.getProperty("admin.login"),
                          config.getProperty("admin.md5pwd"));
    }

    @Override
    public DataPacket handle(DataPacket dataPacket) {
        SEC_CTX.EXECUTION_CTX.set(dataPacket, this.dbEngine.getExecutionContext());
        try {
            dataPacket = this.handlerInspector.process(dataPacket);
        } catch (Exception e){
            // TODO: Return soft exception.
            throw new RuntimeException(e);
        } finally {
            ((ExecutionContext)SEC_CTX.EXECUTION_CTX.get(dataPacket)).close();
            SEC_CTX.EXECUTION_CTX.set(dataPacket, null);
        }
        return dataPacket;
    }

    private void initDatabase(String adminLogin, String adminMd5Pwd) {
        this.MODEL.createAll(this.dbEngine);
        initializeAdmin(adminLogin, adminMd5Pwd);
    }

    private void initializeAdmin(String adminLogin, String adminMd5Pwd) {
        Statement deleteStatement =
                delete("users")
                        .where(eq("admin", Boolean.TRUE));
        Statement insertStatement =
                insert("users")
                        .values(cv("login", adminLogin),
                                cv("username", "Administrator"),
                                cv("admin", Boolean.TRUE),
                                cv("md5pwd", adminMd5Pwd));
        ExecutionContext ectx = this.dbEngine.getExecutionContext();
        ectx.txBegin();
        ectx.execute(deleteStatement);
        ectx.execute(insertStatement);
        ectx.txCommit();
        ectx.close();
    }
}

