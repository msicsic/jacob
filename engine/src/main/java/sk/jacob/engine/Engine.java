package sk.jacob.engine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import sk.jacob.engine.handler.HandlerInspector;
import sk.jacob.engine.json.MessageTypeVersionDeserializer;
import sk.jacob.engine.types.DataPacket;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class Engine implements Module{
    private final Map<String, Method> handlerMap;
    private final Gson gsonTypeVersion =  new GsonBuilder().registerTypeAdapter(String.class,
            new MessageTypeVersionDeserializer()).create();
    private final Gson gson =  new Gson();

    public Engine(List<Class> handlers) {
        this.handlerMap = HandlerInspector.mapHandlers(handlers);
    }

    @Override
    public DataPacket handle(DataPacket dataPacket) {
        String rawRequest = dataPacket.message.rawRequest;
        String rawResponse = null;
        String handlerKey = this.gsonTypeVersion.fromJson(rawRequest, String.class);
        //Handler.Req1 request = this.gson.fromJson(json, Handler.Req1.class);
        Method handler = this.handlerMap.get(handlerKey);
        Class hc = handler.getDeclaringClass();
        try {
            Object h = hc.newInstance();
            dataPacket = (DataPacket)handler.invoke(h, new Object[]{dataPacket});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataPacket;
    }
}
