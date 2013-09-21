package sk.jacob.engine;

import sk.jacob.appcommon.types.ExecutionContext;

public interface Application {
    ExecutionContext onRequest(ExecutionContext ec);
    void onException(ExecutionContext ec, Exception e);
}
