package sk.jacob.engine.handler;

import com.google.gson.Gson;
import sk.jacob.appcommon.types.ExecutionContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public abstract class HandlerRegistry<PAYLOAD> {
    protected static final Gson GSON = new Gson();
    private final Class<? extends Annotation> supportedAnnotation = Handler.class;
    private final Class<PAYLOAD> payloadSuperClass;
    protected Map<String, HandlerConfig> handlerMap = new HashMap<>();

    protected HandlerRegistry(Class<PAYLOAD> payloadSuperClass, List<Class> handlerClasses) {
        this.payloadSuperClass = payloadSuperClass;
        this.handlerMap = mapHandlers(handlerClasses);
    }

    private Map<String, HandlerConfig> mapHandlers(List<Class> handlers) {
        Map<String, HandlerConfig> mappedHandlers = new HashMap<>();
        for (Class<?> handler : handlers) {
            mappedHandlers.putAll(inspect(handler));
        }
        return mappedHandlers;
    }

    private Map<String, HandlerConfig> inspect(Class<?> handlerClass) {
        Map<String, HandlerConfig> mappedHandlers = new HashMap<>();
        for (Method method : handlerClass.getDeclaredMethods()) {
            if (isHandler(method)) {
                HandlerConfig handlerConfig = HandlerConfig.getInstance(method, payloadSuperClass);
                mappedHandlers.put(handlerConfig.handlerKey, handlerConfig);
                break;
            }
        }
        return mappedHandlers;
    }

    private boolean isHandler(Method method) {
        return method.isAnnotationPresent(supportedAnnotation);
    }

    public final ExecutionContext process(ExecutionContext ec) {
        String handlerKey = this.getMessageType(ec);
        return invokeMethod(handlerKey, ec);
    }

    private ExecutionContext invokeMethod(String handlerKey, ExecutionContext ec) {
        if (this.handlerMap.containsKey(handlerKey)) {
            try {
                HandlerConfig handlerConfig = this.handlerMap.get(handlerKey);
                Method handler = handlerConfig.handler;
                processPayload(ec, handlerConfig.payloadClass);
                ec = (ExecutionContext) handler.invoke(null, ec);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return ec;
    }

    /**
     * Gets actual type of message.
     * Handler type is then used by framework to lookup handler method.
     */
    protected abstract String getMessageType(ExecutionContext ec);

    /**
     * Deserializes JSON raw request to corespondent Java object type.
     */
    protected abstract void processPayload(ExecutionContext ec, Class<PAYLOAD> payloadClass);
}
