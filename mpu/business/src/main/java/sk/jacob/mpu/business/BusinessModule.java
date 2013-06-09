package sk.jacob.mpu.business;

import sk.jacob.engine.Engine;
import sk.jacob.engine.types.DataPacket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BusinessModule implements sk.jacob.engine.Module {
    private static List<Class> handlers = new ArrayList<Class>();
    private static Engine engine;

    static {
        handlers.addAll(Arrays.asList(sk.jacob.mpu.business.settings.Init.handlers));
        engine = new Engine(handlers);
    }

    @Override
    public DataPacket handle(DataPacket dataPacket) {
        return engine.handle(dataPacket);
    }
}
