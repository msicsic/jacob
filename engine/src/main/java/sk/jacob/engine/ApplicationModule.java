package sk.jacob.engine;

import sk.jacob.appcommon.types.ExecutionContext;

public interface ApplicationModule {
    ExecutionContext handle(ExecutionContext ec);
}
