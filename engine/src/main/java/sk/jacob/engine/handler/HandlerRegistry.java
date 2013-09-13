package sk.jacob.engine.handler;

import com.google.gson.Gson;
import sk.jacob.appcommon.types.ExecutionContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public abstract class HandlerRegistry<T extends Annotation, K> {
    protected static final Gson GSON = new Gson();
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
                Class<K> payloadClass = queryRequestClass(handler);
                processRequestClass(ec, payloadClass);
                ec = (ExecutionContext) handler.invoke(null, ec);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return ec;
    }

    private Class<K> queryRequestClass(Method handler) {
        for (Class<?> clazz : handler.getParameterTypes()) {
            ParameterizedType superclass = (ParameterizedType)getClass().getGenericSuperclass();
            Class<K> genericClass = (Class<K>) superclass.getActualTypeArguments()[0];
            if (clazz.getSuperclass().equals(genericClass)) {
                return genericClass;
            } else {
                return null;
            }
        }
        return null;
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
    protected abstract void processRequestClass(ExecutionContext ec, Class<K> payloadClass);
}
