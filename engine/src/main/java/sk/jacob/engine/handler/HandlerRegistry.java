package sk.jacob.engine.handler;

import com.google.gson.Gson;
import sk.jacob.appcommon.types.ExecutionContext;
import sk.jacob.appcommon.types.ResponseData;
import sk.jacob.appcommon.types.Return;
import sk.jacob.engine.handler.annotation.Resource;

import java.lang.annotation.Annotation;
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
                Object payload = mapPayload(ec, handlerConfig.payloadClass);
                Object[] callArguments = createCallArguments(handlerConfig, payload, ec);
                ResponseData responseData = (ResponseData) handler.invoke(null, callArguments);
                ec = Return.FIN_RESPONSE(responseData, ec);
            } catch (InvocationTargetException e){
                throw new RuntimeException(e.getTargetException());
            } catch( IllegalAccessException e ) {
                throw new RuntimeException(e);
            }
        }
        return ec;
    }

    private Object[] createCallArguments(HandlerConfig handlerConfig, Object payload, ExecutionContext ec) {
        int argsLength = handlerConfig.parameterAnnotations.length;
        Object[] callArgshandler = new Object[argsLength];
        Annotation[][] parameterAnnotations = handlerConfig.parameterAnnotations;
        for(int i = 0; i < argsLength; i++ ) {
            if (parameterAnnotations[i].length == 0)
                throw new RuntimeException(
                        "Parameter without annotation found in handler ["
                        + handlerConfig.handler
                        + "] at parameter index ["
                        + i
                        + "]");
            Class<?> annotationType = parameterAnnotations[i][0].annotationType();
            //TODO: Factor out.
            if (annotationType.equals(HandlerConfig.PAYLOAD_MARKER)) {
                callArgshandler[i] = payload;
            } else if (annotationType.equals(HandlerConfig.RESOURCE_MARKER)) {
                String payloadLocation = ((Resource)parameterAnnotations[i][0]).location();
                callArgshandler[i] = ec.INSTANCE.get(payloadLocation);
            } else {
                throw new RuntimeException(
                        "Unknown annotation found in handler ["
                                + handlerConfig.handler
                                + "] at parameter index ["
                                + i
                                + "]");
            }
        }
        return callArgshandler;
    }

    /**
     * Gets actual type of message.
     * Handler type is then used by framework to lookup handler method.
     */
    protected abstract String getHandlerKeyFromMessage(ExecutionContext ec);

    /**
     * Maps message payload to module specific class.
     */
    protected abstract Object mapPayload(ExecutionContext ec, Class<?> payloadClass);
}
