package sk.jacob.mpu.context;

import sk.jacob.mpu.context.model.ContextModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import sk.jacob.common.CONTEXT;
import sk.jacob.engine.Module;
import sk.jacob.engine.handler.HandlerInspector;
import sk.jacob.engine.handler.Message;
import sk.jacob.sql.engine.Connection;
import sk.jacob.types.DATAPACKET_STATUS;
import sk.jacob.types.DataPacket;
import sk.jacob.sql.Metadata;
import sk.jacob.sql.engine.DbEngine;
import sk.jacob.types.Return;

public class ContextModule implements Module {
    private static final List<Class> HANDLERS = new ArrayList<>();
    private static final Metadata MODEL = ContextModel.metadata();
    private final DbEngine dbEngine;

    static {
        HANDLERS.addAll(Arrays.asList(sk.jacob.mpu.context.tenant.Init.HANDLERS));
        HANDLERS.addAll(Arrays.asList(sk.jacob.mpu.context.devel.Init.HANDLERS));
    }
    private final HandlerInspector<Message> handlerInspector;

    public ContextModule(Properties config) {
        this.handlerInspector = new ContextHandleInspector(HANDLERS);
        this.dbEngine = new DbEngine(
                config.getProperty("context.url"),
                config.getProperty("context.username"),
                config.getProperty("context.password"));
        this.initDatabase();
    }

    @Override
    public DataPacket handle(DataPacket dataPacket) {
        if (dataPacket.dataPacketStatus == DATAPACKET_STATUS.FIN)
            return dataPacket;

        Connection conn = this.dbEngine.getConnection();
        CONTEXT.CONNECTION.set(dataPacket, conn);

        try {
            conn.txBegin();
            dataPacket = this.handlerInspector.process(dataPacket);
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