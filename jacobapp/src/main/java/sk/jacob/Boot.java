package sk.jacob;

import sk.jacob.connector.http.Connector;
import sk.jacob.connector.http.Jetty;
import sk.jacob.engine.Engine;
import sk.jacob.mpu.business.Module;

public class Boot {
    public static void main(String[] args) {
        Engine engine = new Engine(Module.getHandlers());
        Connector connector = new Jetty();
        connector.start();

        String json =
                "{'type': 'business.uom.add'," +
                " 'version': '0.1'," +
                " 'i': 1," +
                " 's': 'hello'}";
        engine.process(json);

        String json2 =
                "{'type': 'business.uom.add'," +
                " 'version': '0.2'," +
                " 'i': 2," +
                " 's': 'world'}";
        engine.process(json2);
    }
}
