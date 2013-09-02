package sk.jacob.common;

import java.util.HashMap;
import java.util.Map;
import sk.jacob.types.ExecutionContext;

public enum CONTEXT {
    CONNECTION, LDS_BDS;

    private static final String CONTEXT_KEY = "BUSSINESS_CONTEXT";

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
}
