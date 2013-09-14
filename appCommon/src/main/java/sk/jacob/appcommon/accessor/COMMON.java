package sk.jacob.appcommon.accessor;

import sk.jacob.appcommon.types.*;

public class COMMON<T> extends ExecutionContextAccessor<T> {
    public static final String CONTEXT_KEY = "COMMON_CONTEXT";

    public static COMMON<Message> MESSAGE = new COMMON<>("MESSAGE");

    public COMMON(String key) {
        super(key, CONTEXT_KEY);
    }

    public static Message createMessage(String rawRequest) {
        Message message;
        message = new Message();
        message.rawRequest = rawRequest;
        return message;
    }
}
