package sk.jacob;

import sk.jacob.appcommon.ApplicationException;
import sk.jacob.appcommon.accessor.COMMON;
import sk.jacob.appcommon.types.Interrupt;
import sk.jacob.appcommon.types.Return;
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
        } catch (ApplicationException ae) {
            ec = toErrorMessage(ec, ae);
        } catch (Throwable t) {
            ec = onException(ec, t);
        } finally {
            return ec;
        }
    }

    @Override
    public ExecutionContext onException(ExecutionContext ec, Throwable t) {
        // TODO: setup interrupted message
        //Return.INTERRUPT(Interrupt.Type.EXCEPTION, "ass", t, ec);
        return ec;
    }

    public ExecutionContext toErrorMessage(ExecutionContext ec, ApplicationException ae) {
        COMMON.MESSAGE.storeValue(ae.message, ec);
        return ec;
    }
}
