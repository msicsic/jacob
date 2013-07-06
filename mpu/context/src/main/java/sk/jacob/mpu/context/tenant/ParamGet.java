package sk.jacob.mpu.context.tenant;

import java.util.Map;
import java.util.Set;
import sk.jacob.annotation.Required;
import sk.jacob.engine.handler.Message;
import sk.jacob.types.DataPacket;
import sk.jacob.types.RequestDataType;
import sk.jacob.types.ResponseDataType;

public class ParamGet {
    private static class ParamGetReqd extends RequestDataType {
        @Required
        public String tenantId;
        public Set<String> paramNames;
    }

    private static class ParamGetResd extends ResponseDataType {
        @Required
        public Map<String, String> paramValues;
    }

    @Message(type = "context.tenant.paramGet",
            version = "1.0",
            reqd = ParamGet.ParamGetReqd.class,
            resd = ParamGet.ParamGetResd.class)
    public static DataPacket handle(DataPacket dataPacket) {
        return dataPacket;
    }
}
