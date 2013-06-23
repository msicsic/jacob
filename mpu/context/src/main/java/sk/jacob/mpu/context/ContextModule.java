package sk.jacob.mpu.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import sk.jacob.engine.Module;
import sk.jacob.engine.handler.HandlerInspector;
import sk.jacob.engine.handler.Message;
import sk.jacob.engine.types.DataPacket;
import sk.jacob.mpu.context.tenant.Init;

public class ContextModule implements Module {
    private static final List<Class> HANDLERS = new ArrayList<>();

    static {
        HANDLERS.addAll(Arrays.asList(Init.HANDLERS));
    }

    private final HandlerInspector<Message> handlerInspector;

    public ContextModule() {
        this.handlerInspector = new ContextHandleInspector(HANDLERS);
    }

    @Override
    public DataPacket handle(DataPacket dataPacket) {
        return this.handlerInspector.process(dataPacket);
    }
}
