package sk.jacob.mpu.context;

import sk.jacob.common.CONFIG;
import sk.jacob.mpu.context.model.ContextModel;

import java.util.*;

import sk.jacob.common.CONTEXT;
import sk.jacob.engine.Module;
import sk.jacob.engine.handler.HandlerRegistry;
import sk.jacob.engine.handler.Message;
import sk.jacob.sql.engine.Connection;
import sk.jacob.types.DATAPACKET_STATUS;
import sk.jacob.types.DataPacket;
import sk.jacob.sql.Metadata;
import sk.jacob.sql.engine.DbEngine;
import sk.jacob.types.Return;

public class ContextModule implements Module {
    private static final List<Class> HANDLERS = new ArrayList<>();
    private static final Metadata MODEL = ContextModel.INSTANCE.METADATA;
    private final DbEngine dbEngine;
    private final Properties config;

    static {
        HANDLERS.addAll(Arrays.asList(sk.jacob.mpu.context.tenant.Init.HANDLERS));
        HANDLERS.addAll(Arrays.asList(sk.jacob.mpu.context.devel.Init.HANDLERS));
    }

    private final HandlerRegistry<Message> handlerRegistry;

    public ContextModule(Properties config) {
        this.config = config;
        this.handlerRegistry = new ContextHandlerRegistry(HANDLERS);
        this.dbEngine = new DbEngine(CONFIG.CONTEXT_URL.get(config),
                                     CONFIG.CONTEXT_USERNAME.get(config),
                                     CONFIG.CONTEXT_PASSWORD.get(config));
        this.initDatabase();
    }

    @Override
    public DataPacket handle(DataPacket dataPacket) {
        if (dataPacket.status == DATAPACKET_STATUS.FIN)
            return dataPacket;

        Connection conn = this.dbEngine.getConnection();
        CONTEXT.CONNECTION.set(dataPacket, conn);
        CONTEXT.LDS_BDS.set(dataPacket, new HashMap<CONFIG, String>() {{
            put(CONFIG.LDS_BDS_TEMPLATE_URL, CONFIG.LDS_BDS_TEMPLATE_URL.get(config));
            put(CONFIG.LDS_BDS_TEMPLATE_USERNAME, CONFIG.LDS_BDS_TEMPLATE_USERNAME.get(config));
            put(CONFIG.LDS_BDS_TEMPLATE_PASSWORD, CONFIG.LDS_BDS_TEMPLATE_PASSWORD.get(config));
        }});

        try {
            conn.txBegin();
            dataPacket = this.handlerRegistry.process(dataPacket);
            conn.txCommit();
        } catch (Exception e) {
            conn.txRollback();
            dataPacket = Return.EXCEPTION("context.general.context.exception", e, dataPacket);
        } finally {
            conn.close();
        }

        return dataPacket;
    }

    private void initDatabase() {
        MODEL.createAll(this.dbEngine);
    }
}