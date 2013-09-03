package sk.jacob.engine.handler;

import sk.jacob.annotation.Required;
import sk.jacob.types.ExecutionContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
            mappedHandlers.put(getHandlerKey(annotation), method);
        }
        return mappedHandlers;
    }

    public final ExecutionContext process(ExecutionContext executionContext) {
        String handlerKey = this.getMessageType(executionContext);
        return invokeMethod(handlerKey, executionContext);
    }

    private ExecutionContext invokeMethod(String handlerKey, ExecutionContext executionContext) {
        if (this.handlerMap.containsKey(handlerKey)) {
            try {
                Method handler = this.handlerMap.get(handlerKey);
                deserializeMessageElement(executionContext, handler.getAnnotation(this.supportedAnnotation));
                executionContext = (ExecutionContext) handler.invoke(null, executionContext);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return executionContext;
    }

    /**
     * Gets Handler key from given annotation.
     * Method is called at startup when framework builds up handler map.
     */
    protected abstract String getHandlerKey(T annotation);

    /**
     * Gets actual type of message.
     * Signature type is then used by framework to lookup handler method.
     */
    protected abstract String getMessageType(ExecutionContext executionContext);

    /**
     * Deserializes JSON raw request to corespondent Java object type.
     */
    protected abstract void deserializeMessageElement(ExecutionContext executionContext, Annotation annotation);

// TODO: Bude treba vyfaktorovat.
//////////////////////////////////////////////////////////////////////////////////
    private static final Set<Class> BOXED_TYPES = initBoxedTypes();

    private static Set<Class> initBoxedTypes() {
        Set<Class> set = new HashSet<>();
        set.add(Byte.class);
        set.add(Character.class);
        set.add(Short.class);
        set.add(Integer.class);
        set.add(Long.class);
        set.add(Float.class);
        set.add(Double.class);
        set.add(Boolean.class);
        set.add(String.class);
        return set;
    }

    public static Map<String, Object> serializeMethod(Method method) {
        Map<String, Object> handlerMap = new HashMap<>();
        Signature messageAnnotation = method.getAnnotation(Signature.class);

        handlerMap.put("id", messageAnnotation.type());
        handlerMap.put("version", messageAnnotation.version());
        handlerMap.put("resd", serializeClass(messageAnnotation.reqd()));
        handlerMap.put("reqd", serializeClass(messageAnnotation.resd()));

        return handlerMap;
    }

    //TODO pada pri java.util.Date
    private static Map<String, Object> serializeClass(Class resReqClass) {
        Map<String, Object> resMap = new HashMap<>();

        for (Field field : getAllFields(resReqClass)) {
            Class fieldClass = field.getType();

            if (field.isEnumConstant() || field.isSynthetic() || (field.getModifiers() & Modifier.STATIC) != 0) {
                continue;
            }

            Object fieldObj;
            if (fieldClass.isPrimitive() || BOXED_TYPES.contains(fieldClass)) {
                fieldObj = serializePrimitive(fieldClass, field);
            } else if (fieldClass.isArray()) {
                fieldObj = serializeArray(fieldClass);
            } else if (Iterable.class.isAssignableFrom(fieldClass)) {
                fieldObj = serializeIterable(field);
            } else if (Map.class.isAssignableFrom(fieldClass)) {
                fieldObj = new Object();
            } else {
                fieldObj = serializeClass(fieldClass);
            }
            resMap.put(field.getName(), fieldObj);
        }
        return resMap;
    }

    private static Set<Field> getAllFields(Class fieldClass) {
        Set<Field> fields = new HashSet<>();
        for (Class<?> c = fieldClass; c != null; c = c.getSuperclass()) {
            Collections.addAll(fields, c.getDeclaredFields());
        }
        return fields;
    }

    private static Map<String, Object> serializePrimitive(Class fieldClass, Field field) {
        return serializeLengthAnnotation(field.getAnnotation(Length.class),
                serializeHeader(fieldClass, field, new HashMap<String, Object>()));
    }

    private static Map<String, Object> serializeHeader(Class fieldClass, Field field, Map<String, Object> map) {
        map.put("type", fieldClass.getSimpleName());
        map.put("required", field.getAnnotation(Required.class) != null);
        return map;
    }

    private static Map<String, Object> serializeLengthAnnotation(Length lengthAnnot, Map<String, Object> map) {
        if (lengthAnnot != null) {
            map.put("minLength", lengthAnnot.min());
            map.put("maxLength", lengthAnnot.max());
        }
        return map;
    }

    private static Set<Object> serializeArray(Class fieldClass) {
        Set<Object> set = new HashSet<>();
        set.add(serializeClass(fieldClass.getComponentType()));
        return set;
    }

    private static Set<Object> serializeIterable(Field field) {
        Set<Object> set = new HashSet<>();
        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
        set.add(serializeClass((Class) parameterizedType.getActualTypeArguments()[0]));
        return set;
    }
}
