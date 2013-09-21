package sk.jacob;

import sk.jacob.engine.Application;
import sk.jacob.engine.IApplicationModule;
import sk.jacob.mpu.business.BusinessApplicationModule;
import sk.jacob.mpu.context.ContextApplicationModule;
import sk.jacob.mpu.security.SecurityApplicationModule;
import sk.jacob.appcommon.types.ExecutionContext;
import java.util.*;

public class JacobApplication implements Application {
    private final List<IApplicationModule> moduleSequence = new ArrayList<>();

    public JacobApplication(Properties config) {
        initModules(config);
    }

    private void initModules(final Properties config) {
        moduleSequence.add(new SecurityApplicationModule(config));
        moduleSequence.add(new ContextApplicationModule(config));
        moduleSequence.add(new BusinessApplicationModule());
    }

    @Override
    public ExecutionContext onRequest(ExecutionContext ec) {
        try {
            for(IApplicationModule applicationModule : moduleSequence) {
                ec = applicationModule.onRequest(ec);
            }
        } catch (RuntimeException e) {
            onException(ec, e);
        } finally {
            return ec;
        }
    }

    @Override
    public void onException(ExecutionContext ec, Exception e) {
        e.getCause();
    }
}
