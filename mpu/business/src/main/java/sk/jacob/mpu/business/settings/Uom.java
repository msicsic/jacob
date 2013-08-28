package sk.jacob.mpu.business.settings;

import sk.jacob.common.MESSAGE;
import sk.jacob.engine.handler.Message;
import sk.jacob.types.DataPacket;
import sk.jacob.types.RequestDataType;
import sk.jacob.types.ResponseDataType;

public class Uom {
    public class Req1 extends RequestDataType {
        private int i;
        private String s;

        @Override
        public String toString() {
            return  "i="+i+"\ns="+s;
        }
    }

    public class Res1 extends ResponseDataType {
    }

    @Message(type="business.uom.add",
             version="0.1",
             reqd=Req1.class,
             resd=Res1.class)
    public DataPacket method1(DataPacket dataPacket) {
        System.out.println("===========================================");
        System.out.println("method1");
        System.out.println(MESSAGE.current(dataPacket).rawRequest);
        System.out.println(MESSAGE.current(dataPacket).request);
        return dataPacket;
    }

    @Message(type="business.uom.add",
             version="0.2",
             reqd=Req1.class,
             resd=Res1.class)
    public DataPacket method2(DataPacket dataPacket) {
        System.out.println("===========================================");
        System.out.println("method2");
        System.out.println(MESSAGE.current(dataPacket).rawRequest);
        System.out.println(MESSAGE.current(dataPacket).request);
        return dataPacket;
    }
}
