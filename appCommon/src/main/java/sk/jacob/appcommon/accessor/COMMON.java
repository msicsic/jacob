package sk.jacob.appcommon.accessor;

import sk.jacob.appcommon.types.ExecutionContext;
import sk.jacob.appcommon.types.Message;

import java.util.HashMap;
import java.util.Map;

public enum COMMON {
    MESSAGE,
    BUS;

    private static final String CONTEXT_KEY = "MESSAGE_CONTEXT";

    public Object get(ExecutionContext ec) {
        Map<String, Object> bc = ec.INSTANCE.get(CONTEXT_KEY);
        return bc.get(this.name());
    }

    public void set(ExecutionContext ec, Object value) {
        if(ec.INSTANCE.containsKey(CONTEXT_KEY) == false) {
            ec.INSTANCE.put(CONTEXT_KEY, new HashMap<String, Object>());
        }
        Map<String, Object> bc = ec.INSTANCE.get(CONTEXT_KEY);
        bc.put(this.name(), value);
    }

    public static ExecutionContext createDataPacket(String rawRequest) {
        ExecutionContext ec = new ExecutionContext();
        Message message;
        message = new Message();
        message.rawRequest = rawRequest;
        COMMON.MESSAGE.set(ec, message);
        return ec;
    }

    public static Message getMessage(ExecutionContext ec) {
        return (Message)COMMON.MESSAGE.get(ec);
    }
}
