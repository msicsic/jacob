package sk.jacob.appcommon.accessor;

import sk.jacob.appcommon.types.*;

public class COMMON {
    public static ECAcessor<Message> MESSAGE = new ECAcessor<>("/COMMON/MESSAGE");
    public static ECAcessor<ExecutionContext> EXECUTION_CONTEXT = new ECAcessor<>("/COMMON/EXECUTION_CONTEXT");

    public static Message createMessage(String rawRequest) {
        Message message;
        message = new Message();
        message.rawRequest = rawRequest;
        return message;
    }
}