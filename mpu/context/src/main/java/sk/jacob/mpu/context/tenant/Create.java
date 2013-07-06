package sk.jacob.mpu.context.tenant;

import java.util.Map;
import sk.jacob.annotation.Required;
import sk.jacob.engine.handler.Message;
import sk.jacob.types.DataPacket;
import sk.jacob.types.RequestDataType;
import sk.jacob.types.ResponseDataType;

public class Create {
    private static class CreateReqd extends RequestDataType {
        @Required
        public String name;
        public Map<String, String> params;
    }

    private static class CreateResd extends ResponseDataType {
        @Required
        public String tenantId;
        @Required
        public String tenantName;
    }

    @Message(type = "context.tenant.create",
            version = "1.0",
            reqd = Create.CreateReqd.class,
            resd = Create.CreateResd.class)
    public static DataPacket handle(DataPacket dataPacket) {
        return dataPacket;
    }
}
