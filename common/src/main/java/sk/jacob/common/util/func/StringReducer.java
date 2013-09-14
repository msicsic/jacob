package sk.jacob.common.util.func;

import java.util.HashMap;
import java.util.Map;

public class StringReducer implements Functional.Reducer {
    private final String delimiter;
    private static final Map<String, StringReducer> reducers = new HashMap<>();

    private StringReducer(String delimiter) {
        this.delimiter = delimiter;
    }

    public static StringReducer instance(String delimiter) {
        if (!reducers.containsKey(delimiter)) {
            reducers.put(delimiter, new StringReducer(delimiter));
        }
        return reducers.get(delimiter);
    }

    @Override
    public Object reduce(Object accumulator, Object object) {
        StringBuffer b = new StringBuffer(accumulator.toString());
        b.append(this.delimiter);
        b.append(object.toString());
        return b.toString();
    }
}
