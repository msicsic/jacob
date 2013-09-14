package sk.jacob.mpu.business;

import sk.jacob.engine.IApplicationModule;
import sk.jacob.appcommon.types.ExecutionContext;
import sk.jacob.mpu.business.settings.Uom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BusinessApplicationModule implements IApplicationModule {
    private static final List<Class> HANDLERS = new ArrayList<>();

    static {
        HANDLERS.add(Uom.class);
    }

    @Override
    public ExecutionContext onRequest(ExecutionContext ec) {
        return ec;
    }
}
