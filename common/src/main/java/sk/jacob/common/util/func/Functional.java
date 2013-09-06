package sk.jacob.common.util.func;

import java.util.List;

public class Functional {
    public static interface Reducer {
        public Object reduce(Object accumulator, Object object);
    }

    public static Object reduce(Reducer reducer, List elements) {
        return reduce(reducer, elements, null);
    }

    public static Object reduce(Reducer reducer, List elements, Object initializer) {
        Object result = initializer == null ? elements.get(0) : initializer;
        int i = initializer == null ? 1 : 0;
        for(;i < elements.size(); i++) {
            result = reducer.reduce(result, elements.get(i));
        }
        return result;
    }
}
