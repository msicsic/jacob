package sk.jacob.engine;

import sk.jacob.appcommon.types.ExecutionContext;

public class EngineConfig implements IApplicationModule {
    @Override
    public ExecutionContext handle(ExecutionContext ec) {
        return ec;
    }
}
