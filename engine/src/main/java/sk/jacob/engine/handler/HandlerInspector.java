package sk.jacob.engine.handler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HandlerInspector {
    private static final Set<Class> BOXED_TYPES = initBoxedTypes();

    private static Set<Class> initBoxedTypes() {
        Set<Class> set = new HashSet< Class>();
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

    public static List<Object[]> inspect(Class<?> clazz) {
        List<Object[]> methodsFound = new ArrayList<Object[]>();
        for (Method m : clazz.getDeclaredMethods()) {
            if (!m.isAnnotationPresent(Message.class)) {
                continue;
            }
            Message as = m.getAnnotation(Message.class);
            methodsFound.add(new Object[]{as.type() + "." + as.version(), m});
        }
        return methodsFound;
    }

    public static Map<String, Method> mapHandlers(List<Class> handlers) {
        Map<String, Method> mappedHandlers = new HashMap<String, Method>();
        for (Class<?> handler : handlers) {
            for (Object[] methodFound : inspect(handler)) {
                mappedHandlers.put((String) methodFound[0], (Method) methodFound[1]);
            }
        }
        return mappedHandlers;
    }

    public static Map<String, Object> serializeMethod(Method method) {
        Map<String, Object> handlerMap = new HashMap<String, Object>();
        Message messageAnnotation = method.getAnnotation(Message.class);

        handlerMap.put("id", messageAnnotation.type());
        handlerMap.put("version", messageAnnotation.version());
        handlerMap.put("resd", serializeClass(messageAnnotation.reqd()));
        handlerMap.put("reqd", serializeClass(messageAnnotation.resd()));

        return handlerMap;
    }

    //TODO pada pri java.util.Date
    private static Map<String, Object> serializeClass(Class resReqClass) {
        Map<String, Object> resMap = new HashMap<String, Object>();

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
        Set<Field> fields = new HashSet<Field>();
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
        Set<Object> set = new HashSet<Object>();
        set.add(serializeClass(fieldClass.getComponentType()));
        return set;
    }

    private static Set<Object> serializeIterable(Field field) {
        Set<Object> set = new HashSet<Object>();
        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
        set.add(serializeClass((Class) parameterizedType.getActualTypeArguments()[0]));
        return set;
    }
}
