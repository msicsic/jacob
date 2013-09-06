package sk.jacob.appcommon.accessor;

import sk.jacob.appcommon.types.Principal;
import sk.jacob.appcommon.types.Token;

public class SECURITY<T> extends ExecutionContextAccessor<T> {
    public static final String CONTEXT_KEY = "SECURITY_CONTEXT";

    public static SECURITY<Principal> PRINCIPAL = new SECURITY<>("PRINCIPAL");
    public static SECURITY<Token> TOKEN = new SECURITY<>("PRINCIPAL");

    public SECURITY(String key) {
        super(key, CONTEXT_KEY);
    }
}
