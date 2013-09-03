package sk.jacob.engine.handler.devel;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import sk.jacob.engine.handler.HandlerRegistry;
import sk.jacob.types.ExecutionContext;

// FIXME
public class DevelUtil {
    public static final String ROOT_NODE = "jacob";

    public static void mpuListAppendMpu(ExecutionContext ec, Class<?> mpuClass, String moduleId) {
//        MpuRegistryTypes response = initResponse(ec);

//        for (Object[] pair : HandlerRegistry.inspect(mpuClass)) {
//            response.getResd().add(createMpuNode((Method) pair[1], moduleId));
//        }
    }

    private static Map<String, Object> createMpuNode(Method mpuMethod, String moduleId) {
        Map<String, Object> mpuMap = HandlerRegistry.serializeMethod(mpuMethod);
        mpuMap.put("module", moduleId);
        mpuMap.put("type", "message");
        return mpuMap;
    }

    public static void mpuListAppendModule(ExecutionContext ec, String moduleId, String parentId) {
//        MpuRegistryTypes response = initResponse(ec);
//
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("id", moduleId);
//        map.put("parent", parentId);
//        map.put("type", "module");
//        response.getResd().add(map);
    }

    private static MpuRegistryTypes initResponse(ExecutionContext ec) {
//        MpuRegistryTypes response = (MpuRegistryTypes) ec.message.response;
//        if (response == null) {
//            ec.message.response = response = new MpuRegistryTypes();
//            response.setResd(new LinkedList());
//            response.getResd().add(createRootNode());
//        }
//        return response;
        return null;
    }

    private static Object createRootNode() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", ROOT_NODE);
        map.put("type", "root");
        return map;
    }
}
