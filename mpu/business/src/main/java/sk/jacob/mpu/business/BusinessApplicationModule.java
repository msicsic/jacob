package sk.jacob.mpu.business;

import sk.jacob.engine.IApplicationModule;
import sk.jacob.appcommon.types.ExecutionContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BusinessApplicationModule implements IApplicationModule {
    private static final List<Class> HANDLERS = new ArrayList<Class>();

    static {
        HANDLERS.addAll(Arrays.asList(sk.jacob.mpu.business.settings.Init.HANDLERS));
    }

    @Override
    public ExecutionContext handle(ExecutionContext ec) {
        return ec;
    }
}
