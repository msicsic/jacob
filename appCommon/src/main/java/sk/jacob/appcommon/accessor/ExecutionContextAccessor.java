package sk.jacob.appcommon.accessor;

import sk.jacob.appcommon.types.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

public class ExecutionContextAccessor<T>  {
    private final String mapKey;
    private final KeyAccessor<T> keyAccessor;

    public ExecutionContextAccessor(String key, String mapKey) {
        this.keyAccessor = new KeyAccessor<>(key);
        this.mapKey = mapKey;
    }

    public T getFrom(ExecutionContext ec) {
        Map<String, Object> kvMap = ec.INSTANCE.get(mapKey);
        return keyAccessor.getFrom(kvMap);
    }

    public void storeValue(Object value, ExecutionContext ec) {
        if(ec.INSTANCE.containsKey(this.mapKey) == false) {
            ec.INSTANCE.put(this.mapKey, new HashMap<String, Object>());
        }
        Map<String, Object> kvMap = ec.INSTANCE.get(this.mapKey);
        keyAccessor.storeValue(value, kvMap);
    }
}
