package sk.jacob.engine;

import sk.jacob.types.ExecutionContext;

public class EngineConfig implements Module {
    @Override
    public ExecutionContext handle(ExecutionContext executionContext) {
        return executionContext;
    }
}
