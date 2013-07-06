package sk.jacob.engine.handler.devel;

import java.util.Map;
import sk.jacob.annotation.Required;
import sk.jacob.types.RequestDataType;
import sk.jacob.types.ResponseDataType;

public class MpuRegistryTypes {
    public static class MpuItem {
        @Required
        public String id;
        @Required
        public String parent;
        @Required
        public String type;
        public String version;
        public Map<String, Object> resh;
        public Map<String, Object> resd;
    }

    public static class MpuListReqd extends RequestDataType {
    }

    //TODO
    //public static class MpuListResd extends Set<MpuItem> implements ResponseDataType
    public static class MpuListResd extends ResponseDataType {
    }
}
