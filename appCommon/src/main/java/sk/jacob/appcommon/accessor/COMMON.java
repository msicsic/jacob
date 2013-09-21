package sk.jacob.appcommon.accessor;

import sk.jacob.appcommon.types.*;

public class COMMON {
    public static final String EXECUTION_CONTEXT_KEY = "/COMMON/EXECUTION_CONTEXT";
    public static ECAcessor<Message> MESSAGE = new ECAcessor<>("/COMMON/MESSAGE");
    public static ECAcessor<ExecutionContext> EXECUTION_CONTEXT = new ECAcessor<>(EXECUTION_CONTEXT_KEY);
}