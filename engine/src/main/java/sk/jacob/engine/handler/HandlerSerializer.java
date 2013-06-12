package sk.jacob.engine.handler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HandlerSerializer {
    private static final Set<Class> BOXED_TYPES = initBoxedTypes();

    private static Set<Class> initBoxedTypes(){
        return new HashSet<Class>() {{
            add(Byte.class);
            add(Character.class);
            add(Short.class);
            add(Integer.class);
            add(Long.class);
            add(Float.class);
            add(Double.class);
            add(Boolean.class);
            add(String.class);}};
    }

    public static Map<String, Object> serialize(Method method) {
        Map<String, Object> handlerMap = new HashMap<String, Object>();
        Message messageAnnotation = method.getAnnotation(Message.class);

        handlerMap.put("type", messageAnnotation.type());
        handlerMap.put("version", messageAnnotation.version());
        handlerMap.put("resd", serializeClass(messageAnnotation.reqd()));
        handlerMap.put("reqd", serializeClass(messageAnnotation.resd()));

        return handlerMap;
    }

    private static Map<String, Object> serializeClass(Class resReqClass) {
        Map<String, Object> resMap = new HashMap<String, Object>();

        // Handluje zdedene fieldy?
        for (Field field : resReqClass.getDeclaredFields()) {
            Class fieldClass = field.getType();

            if (   fieldClass.getModifiers() == 0
                    || (field.getModifiers() & Modifier.STATIC) != 0)
                continue;

            Object fieldObj;
            if (fieldClass.isPrimitive() || BOXED_TYPES.contains(fieldClass)) {
                fieldObj = handlePrimitive(fieldClass, field);
            } else if (fieldClass.isArray()) {
                fieldObj = handleArray(fieldClass);
            } else if (Iterable.class.isAssignableFrom(fieldClass)) {
                fieldObj = handleIterable(field);
            } else if (Map.class.isAssignableFrom(fieldClass)) {
                fieldObj = new Object();
            } else {
                fieldObj = serializeClass(fieldClass);
            }
            resMap.put(field.getName(), fieldObj);
        }
        return resMap;
    }

    private static Map<String, Object> handlePrimitive(Class fieldClass, Field field) {
        // Prilis genericke
        //
        // String ma minLength, maxLength
        // Cisla minValue, MaxValue
        // Bolo by fajn mat aj regexp, kt by sa podmienene daval do mapy
        return handleLengthAnnotation(field.getAnnotation(Length.class),
                handleHeader(fieldClass, field, new HashMap<String, Object>()));
    }

    private static Map<String, Object> handleHeader(Class fieldClass, Field field, Map<String, Object> map) {
        map.put("type", fieldClass.getSimpleName());
        map.put("required", field.getAnnotation(Required.class) != null);
        return map;
    }

    private static Map<String, Object> handleLengthAnnotation(Length lengthAnnot, Map<String, Object> map) {
        if (lengthAnnot != null) {
            map.put("minLength", lengthAnnot.min());
            map.put("maxLength", lengthAnnot.max());
        }
        return map;
    }

    private static Set<Object> handleArray(Class fieldClass) {
        Set<Object> set = new HashSet<Object>();
        set.add(serializeClass(fieldClass.getComponentType()));
        return set;
    }

    private static Set<Object> handleIterable(Field field) {
        Set<Object> set = new HashSet<Object>();
        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
        set.add(serializeClass((Class) parameterizedType.getActualTypeArguments()[0]));
        return set;
    }
}
