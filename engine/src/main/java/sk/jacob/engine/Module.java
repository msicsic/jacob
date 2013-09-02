package sk.jacob.engine;

import sk.jacob.types.ExecutionContext;

public interface Module {
    ExecutionContext handle(ExecutionContext executionContext);
}
