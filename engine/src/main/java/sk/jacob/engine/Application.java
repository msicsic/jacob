package sk.jacob.engine;

import sk.jacob.appcommon.types.ExecutionContext;

public interface Application {
    ExecutionContext handle(String portId, ExecutionContext ec);
}
