package sk.jacob.engine.handler;

import sk.jacob.engine.handler.annotation.Handler;
import sk.jacob.engine.handler.annotation.Payload;
import sk.jacob.engine.handler.annotation.Resource;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class HandlerConfig {
    public static final Class<Handler> HANDLER_MARKER = Handler.class;
    public static final Class<? extends Annotation> PAYLOAD_MARKER = Payload.class;
    public static final Class<? extends Annotation> RESOURCE_MARKER = Resource.class;
    public final String handlerKey;
    public final Method handler;
    public final Class payloadClass;
    //public final Class<?>[] handlerParameters;
    public final Annotation[][] parameterAnnotations;

    private HandlerConfig(String handlerKey,
                          Method handler,
                          Class payloadClass,
                          //Class<?>[] handlerParameters,
                          Annotation[][] parameterAnnotations){
        this.handlerKey = handlerKey;
        this.handler = handler;
        this.payloadClass = payloadClass;
        //this.handlerParameters = handlerParameters;
        this.parameterAnnotations = parameterAnnotations;
    }

    public static HandlerConfig getInstance(Method handler) {
        Annotation[][] parameterAnnotations = handler.getParameterAnnotations();
        Class<?>[] handlerParameters = handler.getParameterTypes();
        return new HandlerConfig(getHandlerKey(handler),
                                 handler,
                                 lookupPayloadClass(handler, parameterAnnotations, handlerParameters),
                                 //handlerParameters,
                                 parameterAnnotations);
    }

    private static String getHandlerKey(Method handler) {
        String type = handler.getAnnotation(HANDLER_MARKER).type();
        String version = handler.getAnnotation(HANDLER_MARKER).version();
        return (version.trim().length() == 0) ? type.trim()
                                              : type.trim() + "." + version.trim();
    }

    private static Class lookupPayloadClass(Method handler, Annotation[][] parameterAnnotations, Class<?>[] handlerParameters) {
        Class returnValue = null;
        for(int i = 0; i < parameterAnnotations.length ; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            if (parameterAnnotations[i].length == 0)
                throw new RuntimeException(
                        "Parameter without annotation found in handler ["
                                + handler
                                + "] at parameter index ["
                                + i
                                + "]");

            if (annotations[0].annotationType().equals(PAYLOAD_MARKER) ) {
                returnValue = handlerParameters[i];
                break;
            }
        }
        return returnValue;
    }
}
