package sk.jacob.engine.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class HandlerConfig {
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

    public static HandlerConfig getInstance(Method handler, Class payloadSuperType) {
        Class<?>[] handleParameters = handler.getParameterTypes();
        Class<?> payloadClass = queryPayloadClass(handler, handleParameters);
        String handlerKey = null;
        return new HandlerConfig(handlerKey, handler, payloadClass, handleParameters);
    }

    private static Class queryPayloadClass(Method handler, Class<?>[] handlerParameters) {
        Class returnValue = null;
        int i = 0;
        for(Annotation[] annotations : handler.getParameterAnnotations()) {
            if (annotations.length == 1
                && annotations[0].equals(Payload.class) ) {
                returnValue = handlerParameters[i];
                break;
            }
            i++;
        }
        return returnValue;
    }
}
