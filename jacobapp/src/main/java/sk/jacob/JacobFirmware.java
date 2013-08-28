package sk.jacob;

import sk.jacob.common.MESSAGE;
import sk.jacob.engine.Firmware;
import sk.jacob.engine.Module;
import sk.jacob.types.DataPacket;
import sk.jacob.mpu.business.BusinessModule;
import sk.jacob.mpu.context.ContextModule;
import sk.jacob.mpu.security.SecurityModule;

import java.util.*;

import static sk.jacob.util.Log.logger;

public class JacobFirmware implements Firmware {
    public static final String APP_PORT = "APP_PORT";
    public static final String LOGGER_PORT = "LOGGER_PORT";
    private final Map<String, List<Module>> paths = new HashMap<>();

    public JacobFirmware(Properties config) {
        initModules(config);
    }

    private void initModules(final Properties config) {
        paths.put(APP_PORT, Arrays.asList(
                new DataPacketDeserializer(),
                new SecurityModule(config),
                new ContextModule(config),
                new BusinessModule(),
                new DataPacketSerializer()));
    }

    public DataPacket handle(String portId, DataPacket dataPacket) {
        logger(this).info(portId + " --->>> " + MESSAGE.current(dataPacket).rawRequest);
        for(Module module : paths.get(portId)) {
            dataPacket = module.handle(dataPacket);
        }
        logger(this).info(portId + " <<<--- " + MESSAGE.current(dataPacket).rawResponse);
        return dataPacket;
    }
}
