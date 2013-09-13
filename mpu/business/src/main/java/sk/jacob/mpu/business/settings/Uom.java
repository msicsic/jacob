package sk.jacob.mpu.business.settings;

import sk.jacob.engine.handler.DataTypes;
import sk.jacob.appcommon.types.RequestData;
import sk.jacob.appcommon.types.ResponseData;

public class Uom {
    public class Req1 extends RequestData {
        private int i;
        private String s;

        @Override
        public String toString() {
            return  "i="+i+"\ns="+s;
        }
    }

    public class Res1 extends ResponseData {
    }

    @DataTypes(type = "business.uom.add", version = "0.1")
    public Res1 method1(Req1 requestData) {
        System.out.println("===========================================");
        System.out.println("method1");
        return null;
    }

    @DataTypes(type = "business.uom.add", version = "0.2")
    public Res1 method2(Req1 requestData) {
        System.out.println("===========================================");
        System.out.println("method2");
        return null;
    }
}
