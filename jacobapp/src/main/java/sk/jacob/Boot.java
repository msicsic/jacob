package sk.jacob;

import sk.jacob.engine.Engine;
import sk.jacob.mpu.business.Module;

public class Boot {
    public static void main(String[] args) {
        Engine engine = new Engine(Module.getHandlers());

        String json = "{'type': 'business.uom.add'," +
                " 'version': '0.1'," +
                " 'i': 1," +
                " 's': 'hello'}";
        engine.process(json);

        String json2 = "{'type': 'business.uom.add'," +
                " 'version': '0.2'," +
                " 'i': 2," +
                " 's': 'world'}";
        engine.process(json2);
    }
}
