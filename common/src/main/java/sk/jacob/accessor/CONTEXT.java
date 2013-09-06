package sk.jacob.accessor;

import sk.jacob.types.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

public enum CONTEXT {
    CONNECTION, LDS_BDS;

    private static final String CONTEXT_KEY = "BUSSINESS_CONTEXT";

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
}
