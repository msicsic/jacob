package sk.jacob.appcommon.accessor;

import sk.jacob.sql.engine.Connection;

public class CONTEXT<T> extends ExecutionContextAccessor<T> {
    public static final String CONTEXT_KEY = "BUSSINESS_CONTEXT";

    public static CONTEXT<String> LDS_BDS = new CONTEXT<>("LDS_BDS");
    public static CONTEXT<Connection> CONNECTION = new CONTEXT<>("CONNECTION");

    public CONTEXT(String key) {
        super(key, CONTEXT_KEY);
    }
}
