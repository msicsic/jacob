package sk.jacob;

import sk.jacob.engine.Module;
import sk.jacob.engine.types.DataPacket;
import sk.jacob.mpu.business.BusinessModule;
import sk.jacob.mpu.context.ContextModule;
import sk.jacob.mpu.security.SecurityModule;

import java.util.ArrayList;
import java.util.List;

public class JacobFirmware implements Module {
    private final List<Class<? extends Module>> modules;

    public JacobFirmware () {
        this.modules = new ArrayList<Class<? extends Module>>();
        this.modules.add(SecurityModule.class);
        this.modules.add(ContextModule.class);
        this.modules.add(BusinessModule.class);
    }

    public DataPacket handle(DataPacket datapacket) {
        return datapacket;
    }
}
