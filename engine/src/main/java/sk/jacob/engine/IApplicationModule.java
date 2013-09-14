package sk.jacob.engine;

import sk.jacob.appcommon.types.ExecutionContext;

public interface IApplicationModule {
    ExecutionContext handle(ExecutionContext ec);
}
