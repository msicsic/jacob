package sk.jacob.mpu.context.devel;

import sk.jacob.engine.handler.Message;
import sk.jacob.engine.handler.devel.DevelUtil;
import sk.jacob.engine.handler.devel.MpuRegistryTypes;
import sk.jacob.types.DataPacket;

public class MpuList {
    @Message(type = "devel.mpuList",
            version = "0.1",
            reqd = MpuRegistryTypes.MpuListReqd.class,
            resd = MpuRegistryTypes.MpuListResd.class)
    public DataPacket mpuList(DataPacket dataPacket) {
        DevelUtil.mpuListAppendModule(dataPacket, "context", DevelUtil.ROOT_NODE);

        for (Class<?> handler : Init.HANDLERS) {
            DevelUtil.mpuListAppendMpu(dataPacket, handler, "context");
        }

        return dataPacket;
    }
}
