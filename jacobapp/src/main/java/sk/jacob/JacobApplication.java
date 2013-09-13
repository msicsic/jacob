package sk.jacob;

import sk.jacob.appcommon.accessor.COMMON;
import sk.jacob.engine.Application;
import sk.jacob.engine.ApplicationModule;
import sk.jacob.mpu.business.BusinessApplicationModule;
import sk.jacob.mpu.context.ContextApplicationModule;
import sk.jacob.mpu.security.SecurityApplicationModule;
import sk.jacob.appcommon.types.ExecutionContext;

import java.util.*;

import static sk.jacob.common.util.Log.logger;

public class JacobApplication implements Application {
    public static final String APP_PORT = "APP_PORT";
    public static final String LOGGER_PORT = "LOGGER_PORT";
    private final Map<String, List<ApplicationModule>> paths = new HashMap<>();

    public JacobApplication(Properties config) {
        initModules(config);
    }

    private void initModules(final Properties config) {
        paths.put(APP_PORT, Arrays.asList(new MessageDeserializer(),
                                          new SecurityApplicationModule(config),
                                          new ContextApplicationModule(config),
                                          new BusinessApplicationModule(),
                                          new MessageSerializer()));
    }

    public ExecutionContext handle(String portId, ExecutionContext ec) {
        logger(this).info(portId + " --->>> " + COMMON.MESSAGE.getFrom(ec).rawRequest);
        for(ApplicationModule applicationModule : paths.get(portId)) {
            ec = applicationModule.handle(ec);
        }
        logger(this).info(portId + " <<<--- " + COMMON.MESSAGE.getFrom(ec).rawResponse);
        return ec;
    }
}
