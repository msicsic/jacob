package sk.jacob.engine.handler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerInspector {
    public static List<Object[]> inspect(Class<?> clazz) {
        List<Object[]> methodsFound = new ArrayList<Object[]>();
        for(Method m : clazz.getDeclaredMethods()) {
            if (!m.isAnnotationPresent(Message.class))
                continue;
            Message as = m.getAnnotation(Message.class);
            methodsFound.add(new Object[] {as.type()+"."+as.version(), m});
        }
        return methodsFound;
    }

    public static Map<String, Method> mapHandlers(List<Class> handlers) {
        Map<String, Method> mappedHandlers = new HashMap<String, Method>();
        for(Class<?> handler : handlers) {
            for(Object[] methodFound : inspect(handler)) {
                mappedHandlers.put((String) methodFound[0], (Method) methodFound[1]);
            }
        }
        return mappedHandlers;
    }
}
