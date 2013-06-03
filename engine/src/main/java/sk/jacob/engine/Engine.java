package sk.jacob.engine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import sk.jacob.engine.handler.HandlerInspector;
import sk.jacob.engine.json.MessageTypeVersionDeserializer;
import sk.jacob.engine.types.DataPacket;

import java.lang.reflect.Method;
import java.util.Map;

public class Engine {
    private final Map<String, Method> handlerMap;
    private final Gson gsonTypeVersion =  new GsonBuilder().registerTypeAdapter(String.class,
            new MessageTypeVersionDeserializer()).create();
    private final Gson gson =  new Gson();

    public Engine(Class[] handlers) {
        this.handlerMap = HandlerInspector.mapHandlers(handlers);
    }

    public String process(String json) {
        String rawResponse = null;
        String handlerKey = this.gsonTypeVersion.fromJson(json, String.class);
        //Handler.Req1 request = this.gson.fromJson(json, Handler.Req1.class);
        Method handler = this.handlerMap.get(handlerKey);
        Class hc = handler.getDeclaringClass();
        try {
            DataPacket dp = new DataPacket();
            dp.message.rawRequest = json;
            //dp.message.request = request;
            Object h = hc.newInstance();
            DataPacket dpr = (DataPacket)handler.invoke(h, new Object[]{dp});
            rawResponse = dpr.message.rawResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rawResponse;
    }
}
