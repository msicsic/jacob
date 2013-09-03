package sk.jacob.common;

import sk.jacob.types.ExecutionContext;
import sk.jacob.types.Message;

import java.util.HashMap;
import java.util.Map;

public class MESSAGE {
    private static final String CURRENT = "CURRENT";
    private static final String CONTEXT_KEY = "MESSAGE_CONTEXT";

    public static ExecutionContext createDataPacket(String rawRequest) {
        ExecutionContext ec = new ExecutionContext();
        Message message;
        message = new Message();
        message.rawRequest = rawRequest;
        MESSAGE._set(ec, message);
        return ec;
    }

    private static void _set(ExecutionContext ec, Object value) {
        if(ec.INSTANCE.containsKey(CONTEXT_KEY) == false) {
            ec.INSTANCE.put(CONTEXT_KEY, new HashMap<String, Object>());
        }
        Map<String, Object> bc = ec.INSTANCE.get(CONTEXT_KEY);
        bc.put(CURRENT, value);
    }

    public static Message get(ExecutionContext ec) {
        Map<String, Object> bc = ec.INSTANCE.get(CONTEXT_KEY);
        return (Message)bc.get(CURRENT);
    }
}
