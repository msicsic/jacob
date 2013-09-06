package sk.jacob.appcommon.types;

public interface Emitter {
    ExecutionContext send(String portId, ExecutionContext ec);
}
