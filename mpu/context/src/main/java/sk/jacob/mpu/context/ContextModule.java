package sk.jacob.mpu.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import sk.jacob.engine.Module;
import sk.jacob.engine.handler.HandlerInspector;
import sk.jacob.engine.handler.Message;
import sk.jacob.types.DATAPACKET_STATUS;
import sk.jacob.types.DataPacket;
import sk.jacob.mpu.context.tenant.Init;
import sk.jacob.mpu.context.tenant.Model;
import sk.jacob.sql.Metadata;
import sk.jacob.sql.engine.DbEngine;

public class ContextModule implements Module {
    private static final List<Class> HANDLERS = new ArrayList<>();

    private static final Metadata MODEL = Model.get();

    private final DbEngine dbEngine;

    static {
        HANDLERS.addAll(Arrays.asList(Init.HANDLERS));
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
        DataPacket resDataPacket = dataPacket;
        if (dataPacket.dataPacketStatus == DATAPACKET_STATUS.AFP) {
            Context.EXECUTION_CTX.set(dataPacket, this.dbEngine.getExecutionContext());
            try {
                return this.handlerInspector.process(dataPacket);
            } finally {
                Context.clear(dataPacket);
            }
        }
        return resDataPacket;
    }

    private void initDatabase() {
        MODEL.createAll(this.dbEngine);
    }
}