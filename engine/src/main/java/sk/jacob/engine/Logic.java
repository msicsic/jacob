package sk.jacob.engine;

import sk.jacob.appcommon.types.ExecutionContext;

public interface Logic {
    ExecutionContext handle(String portId, ExecutionContext ec);
}
