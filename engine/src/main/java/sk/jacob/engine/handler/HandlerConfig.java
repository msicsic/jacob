package sk.jacob.engine.handler;

import sk.jacob.engine.handler.annotation.Handler;
import sk.jacob.engine.handler.annotation.Payload;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class HandlerConfig {
    public static final Class<Handler> HANDLER_MARKER = Handler.class;
    private static final Class<? extends Annotation> PAYLOAD_MARKER = Payload.class;
    public final String handlerKey;
    public final Method handler;
    public final Class payloadClass;
    public final Class<?>[] handlerParameters;

    private HandlerConfig(String handlerKey,
                          Method handler,
                          Class payloadClass,
                          Class<?>[] handlerParameters){
        this.handlerKey = handlerKey;
        this.handler = handler;
        this.payloadClass = payloadClass;
        this.handlerParameters = handlerParameters;
    }

    public static HandlerConfig getInstance(Method handler) {
        return new HandlerConfig(getHandlerKey(handler),
                                 handler,
                                 lookupPayloadClass(handler),
                                 handler.getParameterTypes());
    }

    private static String getHandlerKey(Method handler) {
        String type = handler.getAnnotation(HANDLER_MARKER).type();
        String version = handler.getAnnotation(HANDLER_MARKER).version();

        return (version.trim().length() == 0) ? type
                                              : type + "." + version;
    }

    // TODO: Needs optimization
    private static Class lookupPayloadClass(Method handler) {
        Class returnValue = null;
        int i = 0;
        for(Annotation[] annotations : handler.getParameterAnnotations()) {
            if (   annotations.length == 1
                && annotations[0].annotationType().equals(PAYLOAD_MARKER) ) {
                Class<?>[] handlerParameters = handler.getParameterTypes();
                returnValue = handlerParameters[i];
                break;
            }
            i++;
        }
        return returnValue;
    }
}
