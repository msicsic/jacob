package sk.jacob.engine;

import sk.jacob.appcommon.types.ExecutionContext;

public class EngineConfig implements Module {
    @Override
    public ExecutionContext handle(ExecutionContext ec) {
        return ec;
    }
}
