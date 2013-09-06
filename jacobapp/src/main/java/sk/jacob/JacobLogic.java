package sk.jacob;

import sk.jacob.appcommon.accessor.COMMON;
import sk.jacob.engine.Logic;
import sk.jacob.engine.Module;
import sk.jacob.mpu.business.BusinessModule;
import sk.jacob.mpu.context.ContextModule;
import sk.jacob.mpu.security.SecurityModule;
import sk.jacob.appcommon.types.ExecutionContext;

import java.util.*;

import static sk.jacob.common.util.Log.logger;

public class JacobLogic implements Logic {
    public static final String APP_PORT = "APP_PORT";
    public static final String LOGGER_PORT = "LOGGER_PORT";
    private final Map<String, List<Module>> paths = new HashMap<>();

    public JacobLogic(Properties config) {
        initModules(config);
    }

    private void initModules(final Properties config) {
        paths.put(APP_PORT, Arrays.asList(new MessageDeserializer(),
                                          new SecurityModule(config),
                                          new ContextModule(config),
                                          new BusinessModule(),
                                          new MessageSerializer()));
    }

    public ExecutionContext handle(String portId, ExecutionContext ec) {
        logger(this).info(portId + " --->>> " + COMMON.MESSAGE.getFrom(ec).rawRequest);
        for(Module module : paths.get(portId)) {
            ec = module.handle(ec);
        }
        logger(this).info(portId + " <<<--- " + COMMON.MESSAGE.getFrom(ec).rawResponse);
        return ec;
    }
}
