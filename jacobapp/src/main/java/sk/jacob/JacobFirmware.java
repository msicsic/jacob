package sk.jacob;

import sk.jacob.engine.Module;
import sk.jacob.engine.types.DataPacket;
import sk.jacob.mpu.business.BusinessModule;
import sk.jacob.mpu.context.ContextModule;
import sk.jacob.mpu.security.SecurityModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JacobFirmware implements Module {
    private final List<Module> modules;

    public JacobFirmware(Properties config) {
        modules = initModules(config);
    }

    private final List<Module> initModules(final Properties config) {
        return new ArrayList<Module>() {{
            add(new DataPacketDeserializer());
            add(new SecurityModule(config));
            add(new ContextModule());
            add(new BusinessModule());
            add(new DataPacketSerializer());
        }};
    }

    public DataPacket handle(DataPacket dataPacket) {
        for(Module module : modules) {
            dataPacket = module.handle(dataPacket);
        }
        return dataPacket;
    }
}
