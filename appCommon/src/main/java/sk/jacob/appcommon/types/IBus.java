package sk.jacob.appcommon.types;

public interface IBus {
    ExecutionContext send(String portId, ExecutionContext ec);
}
