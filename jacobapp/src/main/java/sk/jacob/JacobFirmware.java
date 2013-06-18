package sk.jacob;

import sk.jacob.engine.Module;
import sk.jacob.engine.types.DataPacket;
import sk.jacob.mpu.business.BusinessModule;
import sk.jacob.mpu.context.ContextModule;
import sk.jacob.mpu.security.SecurityModule;

import java.util.ArrayList;
import java.util.List;

public class JacobFirmware implements Module {
    private static final List<Module> MODULES = initModules();

    private static final List<Module> initModules() {
        return new ArrayList<Module>() {{
            add(new SecurityModule());
            add(new ContextModule());
            add(new BusinessModule());
        }};
    }

    public DataPacket handle(DataPacket dataPacket) {
        for(Module module : MODULES) {
            dataPacket = module.handle(dataPacket);
        }
        return dataPacket;
    }
}
