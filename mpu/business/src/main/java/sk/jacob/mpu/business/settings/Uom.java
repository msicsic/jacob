package sk.jacob.mpu.business.settings;

import sk.jacob.common.MESSAGE;
import sk.jacob.engine.handler.Message;
import sk.jacob.types.ExecutionContext;
import sk.jacob.types.RequestData;
import sk.jacob.types.ResponseData;

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

    @Message(type="business.uom.add",
             version="0.1",
             reqd=Req1.class,
             resd=Res1.class)
    public ExecutionContext method1(ExecutionContext executionContext) {
        System.out.println("===========================================");
        System.out.println("method1");
        System.out.println(MESSAGE.current(executionContext).rawRequest);
        System.out.println(MESSAGE.current(executionContext).request);
        return executionContext;
    }

    @Message(type="business.uom.add",
             version="0.2",
             reqd=Req1.class,
             resd=Res1.class)
    public ExecutionContext method2(ExecutionContext executionContext) {
        System.out.println("===========================================");
        System.out.println("method2");
        System.out.println(MESSAGE.current(executionContext).rawRequest);
        System.out.println(MESSAGE.current(executionContext).request);
        return executionContext;
    }
}
