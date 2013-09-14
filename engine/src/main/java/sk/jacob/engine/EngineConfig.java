package sk.jacob.engine;

import sk.jacob.appcommon.types.ExecutionContext;

public class EngineConfig implements IApplicationModule {
    @Override
    public ExecutionContext onRequest(ExecutionContext ec) {
        return ec;
    }
}
