package sk.jacob.appcommon.accessor;

import sk.jacob.sql.engine.Connection;

public class CONNECTION<T> extends ExecutionContextAccessor<T> {
    public static CONNECTION<Connection> CURRENT = new CONNECTION<>("SECURITY_CONNECTION");

    public CONNECTION(String key) {
        super(key, SECURITY.CONTEXT_KEY);
    }
}
