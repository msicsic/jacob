package sk.jacob.common;

import sk.jacob.types.ExecutionContext;
import sk.jacob.types.Principal;

import java.util.HashMap;
import java.util.Map;

public enum SECURITY {
    CONNECTION,
    PRINCIPAL,
    TOKEN;

    private static final String CONTEXT_KEY = "SECURITY_CONTEXT";

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

    public static Principal getPrincipal(ExecutionContext ec) {
        return (Principal)SECURITY.PRINCIPAL.get(ec);
    }

    public static void setPrincipal(ExecutionContext ec, Principal principal) {
        SECURITY.PRINCIPAL.set(ec, principal);
    }
}
