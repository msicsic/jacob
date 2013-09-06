package sk.jacob.engine;

import sk.jacob.appcommon.types.ExecutionContext;

public interface Firmware {
    ExecutionContext handle(String portId, ExecutionContext ec);
}
