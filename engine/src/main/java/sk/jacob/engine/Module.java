package sk.jacob.engine;

import sk.jacob.types.Message;

public interface Module {
    Message handle(Message message);
}
