package sk.jacob.mpu.security;

import sk.jacob.engine.Module;
import sk.jacob.engine.handler.HandlerInspector;
import sk.jacob.engine.handler.Token;
import sk.jacob.engine.types.DataPacket;
import sk.jacob.mpu.security.dbregistry.Init;
import sk.jacob.mpu.security.dbregistry.Model;
import sk.jacob.sql.DbEngine;
import sk.jacob.sql.ExecutionContext;
import sk.jacob.sql.Metadata;
import sk.jacob.sql.Op;
import sk.jacob.sql.dialect.Statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static sk.jacob.sql.CRUD.*;

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
        return this.handlerInspector.process(dataPacket);
    }

    private void initDatabase(String adminLogin, String adminMd5Pwd) {
        MODEL.createAll(dbEngine);
        initializeAdmin(adminLogin, adminMd5Pwd);
    }

    private void initializeAdmin(String adminLogin, String adminMd5Pwd) {
        Statement deleteStatement =
                delete("users")
                        .where(Op.eq("admin", Boolean.TRUE));
        ExecutionContext ectx = this.dbEngine.getExecutionContext();
        ectx.execute(deleteStatement);

        Statement insertStatement =
                insert("users")
                        .values(cv("login", adminLogin),
                                cv("username", "Administrator"),
                                cv("admin", Boolean.TRUE),
                                cv("md5pwd", adminMd5Pwd));
        ectx.execute(insertStatement);
    }
}

