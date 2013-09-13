package sk.jacob.engine.handler;

import sk.jacob.appcommon.types.ExecutionContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public abstract class HandlerRegistry<T extends Annotation> {
    private final Class<T> supportedAnnotation;
    protected Map<String, Method> handlerMap = new HashMap<>();

    protected HandlerRegistry(Class<T> supportedAnnotation, List<Class> messageHandlers) {
        this.supportedAnnotation = supportedAnnotation;
        this.handlerMap = map(messageHandlers);
    }

    private Map<String, Method> map(List<Class> handlers) {
        Map<String, Method> mappedHandlers = new HashMap<>();
        for (Class<?> handler : handlers) {
            mappedHandlers.putAll(inspect(handler));
        }
        return mappedHandlers;
    }

    private Map<String, Method> inspect(Class<?> handler) {
        Map<String, Method> mappedHandlers = new HashMap<>();
        for (Method method : handler.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(supportedAnnotation)) {
                continue;
            }
            T annotation = method.getAnnotation(supportedAnnotation);
            mappedHandlers.put(getHandlerType(annotation), method);
        }
        return mappedHandlers;
    }

    public final ExecutionContext process(ExecutionContext ec) {
        String handlerKey = this.getMessageType(ec);
        return invokeMethod(handlerKey, ec);
    }

    private ExecutionContext invokeMethod(String handlerKey, ExecutionContext ec) {
        if (this.handlerMap.containsKey(handlerKey)) {
            try {
                Method handler = this.handlerMap.get(handlerKey);
                processRequestClass(ec, handler);
                ec = (ExecutionContext) handler.invoke(null, ec);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return ec;
    }

    /**
     * Gets Handler key from given annotation.
     * Method is called at startup when framework builds up handler map.
     */
    protected abstract String getHandlerType(T annotation);

    /**
     * Gets actual type of message.
     * DataTypes type is then used by framework to lookup handler method.
     */
    protected abstract String getMessageType(ExecutionContext ec);

    /**
     * Deserializes JSON raw request to corespondent Java object type.
     */
    protected abstract void processRequestClass(ExecutionContext ec, Method method);
}
