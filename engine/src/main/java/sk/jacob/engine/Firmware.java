package sk.jacob.engine;

import sk.jacob.types.ExecutionContext;

public interface Firmware {
    ExecutionContext handle(String portId, ExecutionContext executionContext);
}
