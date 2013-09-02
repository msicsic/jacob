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

    public Object get(ExecutionContext executionContext) {
        Map<String, Object> bc = executionContext.CONTEXT.get(CONTEXT_KEY);
        return bc.get(this.name());
    }

    public void set(ExecutionContext executionContext, Object value) {
        if(executionContext.CONTEXT.containsKey(CONTEXT_KEY) == false) {
            executionContext.CONTEXT.put(CONTEXT_KEY, new HashMap<String, Object>());
        }
        Map<String, Object> bc = executionContext.CONTEXT.get(CONTEXT_KEY);
        bc.put(this.name(), value);
    }

    public static Principal getPrincipal(ExecutionContext executionContext) {
        return (Principal)SECURITY.PRINCIPAL.get(executionContext);
    }

    public static void setPrincipal(ExecutionContext executionContext, Principal principal) {
        SECURITY.PRINCIPAL.set(executionContext, principal);
    }
}
