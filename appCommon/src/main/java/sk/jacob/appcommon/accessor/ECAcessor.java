package sk.jacob.appcommon.accessor;

import sk.jacob.appcommon.types.ExecutionContext;

public class ECAcessor<T> {
    private final KeyAccessor<T> keyAccessor;

    public ECAcessor(String key) {
        this.keyAccessor = new KeyAccessor<>(key);
    }

    public T getFrom(ExecutionContext ec) {
        return keyAccessor.getFrom(ec.INSTANCE);
    }

    public void storeValue(Object value,ExecutionContext ec) {
        keyAccessor.storeValue(value, ec.INSTANCE);
    }
}
