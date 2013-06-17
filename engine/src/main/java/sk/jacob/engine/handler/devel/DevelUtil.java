package sk.jacob.engine.handler.devel;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import sk.jacob.engine.handler.HandlerInspector;
import sk.jacob.engine.types.DataPacket;

public class DevelUtil {
    public static String ROOT_NODE = "jacob";

    public static void mpuListAppendMpu(DataPacket dataPacket, Class<?> mpuClass, String moduleId) {
        MpuListResponseType response = initResponse(dataPacket);

        for (Object[] pair : HandlerInspector.inspect(mpuClass)) {
            response.getResd().add(createMpuNode((Method) pair[1], moduleId));
        }
    }

    private static Map<String, Object> createMpuNode(Method mpuMethod, String moduleId) {
        Map<String, Object> mpuMap = HandlerInspector.serializeMethod(mpuMethod);
        mpuMap.put("module", moduleId);
        mpuMap.put("type", "message");
        return mpuMap;
    }

    public static void mpuListAppendModule(DataPacket dataPacket, String moduleId, String parentId) {
        MpuListResponseType response = initResponse(dataPacket);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", moduleId);
        map.put("parent", parentId);
        map.put("type", "module");
        response.getResd().add(map);
    }

    private static MpuListResponseType initResponse(DataPacket dataPacket) {
        MpuListResponseType response = (MpuListResponseType) dataPacket.message.response;
        if (response == null) {
            dataPacket.message.response = response = new MpuListResponseType();
            response.setResd(new LinkedList());
            response.getResd().add(createRootNode());
        }
        return response;
    }

    private static Object createRootNode() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", ROOT_NODE);
        map.put("type", "root");
        return map;
    }
}
