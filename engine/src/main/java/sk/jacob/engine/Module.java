package sk.jacob.engine;

import sk.jacob.appcommon.types.ExecutionContext;

public interface Module {
    ExecutionContext handle(ExecutionContext ec);
}
