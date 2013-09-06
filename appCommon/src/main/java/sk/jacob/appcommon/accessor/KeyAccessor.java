package sk.jacob.appcommon.accessor;

import java.util.Map;

public class KeyAccessor<T> {
    private final String key;

    public KeyAccessor(String key) {
        this.key = key;
    }

    public T getFrom(Map<String, Object> kvMap) {
        return (T)kvMap.get(this.key);
    }

    public void storeValue(Object value, Map<String, Object> kvMap) {
        kvMap.put(this.key, value);
    }
}
