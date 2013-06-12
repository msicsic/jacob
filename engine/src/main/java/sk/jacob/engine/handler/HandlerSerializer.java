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

    private static final Set<Class> PRIMITIVE_TYPES = new HashSet<Class>() {
        {
            add(byte.class);
            add(Byte.class);
            add(char.class);
            add(Character.class);
            add(short.class);
            add(Short.class);
            add(int.class);
            add(Integer.class);
            add(long.class);
            add(Long.class);
            add(float.class);
            add(Float.class);
            add(double.class);
            add(Double.class);
            add(boolean.class);
            add(Boolean.class);
            add(String.class);
        }
    };

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

        for (Field field : resReqClass.getDeclaredFields()) {
            Class fieldClass = field.getType();
            if (fieldClass.getModifiers() != 0 && (field.getModifiers() & Modifier.STATIC) == 0) {
                Object fieldObj;
                if (PRIMITIVE_TYPES.contains(fieldClass)) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("type", fieldClass.getCanonicalName());
                    map.put("required", field.getAnnotation(Required.class) != null);
                    Length lengthAnnot = field.getAnnotation(Length.class);
                    if (lengthAnnot != null) {
                        map.put("minLength", lengthAnnot.min());
                        map.put("maxLength", lengthAnnot.max());
                    }
                    fieldObj = map;
                } else if (fieldClass.isArray()) {
                    Set<Object> set = new HashSet<Object>();
                    set.add(serializeClass(fieldClass.getComponentType()));
                    fieldObj = set;
                } else if (Iterable.class.isAssignableFrom(fieldClass)) {
                    ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                    Set<Object> set = new HashSet<Object>();
                    set.add(serializeClass((Class) parameterizedType.getActualTypeArguments()[0]));
                    fieldObj = set;
                } else if (Map.class.isAssignableFrom(fieldClass)) {
                    fieldObj = new Object();
                } else {
                    fieldObj = serializeClass(fieldClass);
                }
                resMap.put(field.getName(), fieldObj);
            }
        }
        return resMap;
    }
}
