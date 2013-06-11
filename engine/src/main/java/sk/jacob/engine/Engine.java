package sk.jacob.engine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import sk.jacob.engine.handler.HandlerInspector;
import sk.jacob.engine.json.MessageTypeVersionDeserializer;
import sk.jacob.engine.types.DataPacket;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Engine implements Module{
    private final Map<String, Method> handlerMap;
    private final Gson gsonTypeVersion =
            new GsonBuilder().registerTypeAdapter(String.class, new MessageTypeVersionDeserializer()).create();
    private final Gson gson =  new Gson();
    private final Map<Class, Object> handlerClasses = new HashMap<Class, Object>();

    public Engine(List<Class> handlers) {
        this.handlerMap = HandlerInspector.mapHandlers(handlers);
    }

    @Override
    public DataPacket handle(DataPacket dataPacket) {
        String rawRequest = dataPacket.message.rawRequest;
        String rawResponse = null;
        String handlerKey = this.gsonTypeVersion.fromJson(rawRequest, String.class);
        //Handler.Req1 request = this.gson.fromJson(json, Handler.Req1.class);
        try {
            Method handler = this.handlerMap.get(handlerKey);
            Object h = handlerInstance(handlerKey);
            dataPacket = (DataPacket)handler.invoke(h, new Object[]{dataPacket});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataPacket;
    }

    private Object handlerInstance(String handlerKey) {
        Method handler = this.handlerMap.get(handlerKey);
        Class clazz = handler.getDeclaringClass();
        if (!handlerClasses.containsKey(clazz)) {
            handlerClasses.put(clazz, newInstance(clazz));
        }
        return  handlerClasses.get(clazz);
    }

    private Object newInstance(Class clazz) {
        Object instance = null;
        try {
            instance = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return instance;
    }
}
