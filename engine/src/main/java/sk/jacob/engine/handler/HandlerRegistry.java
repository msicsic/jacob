package sk.jacob.engine.handler;

import com.google.gson.Gson;
import sk.jacob.appcommon.accessor.COMMON;
import sk.jacob.appcommon.types.ExecutionContext;
import sk.jacob.appcommon.types.ResponseData;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public abstract class HandlerRegistry {
    protected static final Gson GSON = new Gson();
    protected Map<String, HandlerConfig> handlerMap = new HashMap<>();

    protected HandlerRegistry(List<Class> handlerClasses) {
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
                HandlerConfig handlerConfig = HandlerConfig.getInstance(method);
                mappedHandlers.put(handlerConfig.handlerKey, handlerConfig);
                break;
            }
        }
        return mappedHandlers;
    }

    private boolean isHandler(Method method) {
        return method.isAnnotationPresent(HandlerConfig.HANDLER_MARKER);
    }

    public final ExecutionContext process(ExecutionContext ec) {
        String handlerKey = this.getHandlerKeyFromMessage(ec);
        return invokeMethod(handlerKey, ec);
    }

    private ExecutionContext invokeMethod(String handlerKey, ExecutionContext ec) {
        if (this.handlerMap.containsKey(handlerKey)) {
            try {
                HandlerConfig handlerConfig = this.handlerMap.get(handlerKey);
                Method handler = handlerConfig.handler;
                mapPayload(ec, handlerConfig.payloadClass);
                ResponseData responseData = (ResponseData) handler.invoke(null, ec);
                COMMON.MESSAGE.getFrom(ec).response.resd = responseData;
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return ec;
    }

    /**
     * Gets actual type of message.
     * Handler type is then used by framework to lookup handler method.
     */
    protected abstract String getHandlerKeyFromMessage(ExecutionContext ec);

    /**
     * Deserializes JSON raw request to corespondent Java object type.
     */
    protected abstract void mapPayload(ExecutionContext ec, Class<?> payloadClass);
}
