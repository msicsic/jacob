package sk.jacob.common;

import sk.jacob.types.ExecutionContext;
import sk.jacob.types.MessageType;

import java.util.HashMap;
import java.util.Map;

public enum MESSAGE {
    CURRENT;

    private static final String CONTEXT_KEY = "MESSAGE_CONTEXT";

    public Object get(ExecutionContext executionContext) {
        Map<String, Object> bc = executionContext.CONTEXT.get(CONTEXT_KEY);
        return bc.get(this.name());
    }

    public void set(ExecutionContext executionContext, Object value) {
        if(executionContext.CONTEXT.containsKey(CONTEXT_KEY) == false) {
            executionContext.CONTEXT.put(CONTEXT_KEY, new HashMap<String, Object>());
        }
        Map<String, Object> bc = executionContext.CONTEXT.get(CONTEXT_KEY);
        bc.put(this.name(), value);
    }

    public static MessageType current(ExecutionContext executionContext) {
        return (MessageType)MESSAGE.CURRENT.get(executionContext);
    }

    public static ExecutionContext createDataPacket(String rawRequest) {
        ExecutionContext executionContext = new ExecutionContext();
        MessageType message;
        message = new MessageType();
        message.rawRequest = rawRequest;
        MESSAGE.CURRENT.set(executionContext, message);
        return executionContext;
    }

}
