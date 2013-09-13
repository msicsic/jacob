package sk.jacob.appcommon.accessor;

import sk.jacob.appcommon.types.*;

public class COMMON<T> extends ExecutionContextAccessor<T> {
    public static final String CONTEXT_KEY = "COMMON_CONTEXT";

    public static COMMON<Message> MESSAGE = new COMMON<>("MESSAGE");
    public static COMMON<IBus> EMITTER = new COMMON<>("EMITTER");

    public COMMON(String key) {
        super(key, CONTEXT_KEY);
    }

    public static ExecutionContext createDataPacket(String rawRequest) {
        ExecutionContext ec = new ExecutionContext();
        Message message;
        message = new Message();
        message.rawRequest = rawRequest;
        COMMON.MESSAGE.set(message, ec);
        return ec;
    }
}
