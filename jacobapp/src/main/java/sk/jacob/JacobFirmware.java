package sk.jacob;

import sk.jacob.common.MESSAGE;
import sk.jacob.engine.Firmware;
import sk.jacob.engine.Module;
import sk.jacob.engine.handler.Signature;
import sk.jacob.types.ExecutionContext;
import sk.jacob.mpu.business.BusinessModule;
import sk.jacob.mpu.context.ContextModule;
import sk.jacob.mpu.security.SecurityModule;
import sk.jacob.types.Message;

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

    public Message handle(String portId, Message message) {
        logger(this).info(portId + " --->>> " + message.rawRequest);
        for(Module module : paths.get(portId)) {
            message = module.handle(message);
        }
        logger(this).info(portId + " <<<--- " + message.rawResponse);
        return message;
    }
}
