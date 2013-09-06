package sk.jacob.appcommon.accessor;

import sk.jacob.appcommon.types.Principal;
import sk.jacob.appcommon.types.Token;
import sk.jacob.sql.engine.Connection;

public class SECURITY<T> extends ExecutionContextAccessor<T> {
    public static final String CONTEXT_KEY = "SECURITY_CONTEXT";

    public static SECURITY<Principal> PRINCIPAL = new SECURITY<>("PRINCIPAL");
    public static SECURITY<Token> TOKEN = new SECURITY<>("PRINCIPAL");
    public static SECURITY<Connection> CONNECTION = new SECURITY<>("CONNECTION");

    public SECURITY(String key) {
        super(key, CONTEXT_KEY);
    }
}
