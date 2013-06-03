package sk.jacob.mpu.business;

import sk.jacob.engine.JacobModule;
import sk.jacob.engine.types.DataPacket;

public class Module implements JacobModule {
    public static Class[] getHandlers() {
        return sk.jacob.mpu.business.settings.Init.getHandlers();
    }

    @Override
    public DataPacket handle(DataPacket dataPacket) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
