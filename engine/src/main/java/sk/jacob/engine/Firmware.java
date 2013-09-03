package sk.jacob.engine;

import sk.jacob.types.ExecutionContext;
import sk.jacob.types.Message;

public interface Firmware {
    Message handle(String portId, Message message);
}
