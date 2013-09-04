package sk.jacob.mpu.context;

import sk.jacob.accessor.CONFIG;
import sk.jacob.accessor.CONTEXT;
import sk.jacob.engine.Module;
import sk.jacob.engine.handler.DataTypes;
import sk.jacob.engine.handler.HandlerRegistry;
import sk.jacob.mpu.context.model.ContextModel;
import sk.jacob.sql.Metadata;
import sk.jacob.sql.engine.Connection;
import sk.jacob.sql.engine.DbEngine;
import sk.jacob.types.EXECUTION_CONTEXT;
import sk.jacob.types.ExecutionContext;
import sk.jacob.types.Return;

import java.util.*;

import static sk.jacob.util.Log.logger;
import static sk.jacob.util.StackTrace.stackToString;

public class ContextModule implements Module {
    private static final List<Class> HANDLERS = new ArrayList<>();
    private static final Metadata MODEL = ContextModel.INSTANCE.METADATA;
    private final DbEngine dbEngine;
    private final Properties config;
    private final HandlerRegistry<DataTypes> handlerRegistry;

    static {
        HANDLERS.addAll(Arrays.asList(sk.jacob.mpu.context.tenant.Init.HANDLERS));
    }

    public ContextModule(Properties config) {
        this.config = config;
        this.handlerRegistry = new ContextHandlerRegistry(HANDLERS);
        this.dbEngine = new DbEngine(CONFIG.CONTEXT_URL.get(config),
                                     CONFIG.CONTEXT_USERNAME.get(config),
                                     CONFIG.CONTEXT_PASSWORD.get(config));
        this.initDatabase();
    }

    @Override
    public ExecutionContext handle(ExecutionContext ec) {
        if (ec.status == EXECUTION_CONTEXT.FIN)
            return ec;

        Connection conn = this.dbEngine.getConnection();
        CONTEXT.CONNECTION.set(ec, conn);
        CONTEXT.LDS_BDS.set(ec, new HashMap<CONFIG, String>() {{
            put(CONFIG.LDS_BDS_TEMPLATE_URL, CONFIG.LDS_BDS_TEMPLATE_URL.get(config));
            put(CONFIG.LDS_BDS_TEMPLATE_USERNAME, CONFIG.LDS_BDS_TEMPLATE_USERNAME.get(config));
            put(CONFIG.LDS_BDS_TEMPLATE_PASSWORD, CONFIG.LDS_BDS_TEMPLATE_PASSWORD.get(config));
        }});

        try {
            conn.txBegin();
            ec = this.handlerRegistry.process(ec);
            conn.txCommit();
        } catch (Exception e) {
            conn.txRollback();
            String errorCode = "context.general.context.exception";
            logger(this).error(errorCode, e);
            ec = Return.EXCEPTION(errorCode, e, ec);
        } finally {
            conn.close();
        }

        return ec;
    }

    private void initDatabase() {
        MODEL.createAll(this.dbEngine);
    }
}