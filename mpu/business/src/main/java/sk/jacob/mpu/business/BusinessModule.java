package sk.jacob.mpu.business;

import sk.jacob.engine.Module;
import sk.jacob.types.ExecutionContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BusinessModule implements Module {
    private static final List<Class> HANDLERS = new ArrayList<Class>();

    static {
        HANDLERS.addAll(Arrays.asList(sk.jacob.mpu.business.settings.Init.HANDLERS));
    }

    @Override
    public ExecutionContext handle(ExecutionContext ec) {
        return ec;
    }
}
