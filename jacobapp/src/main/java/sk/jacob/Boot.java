package sk.jacob;

import sk.jacob.connector.http.Jetty;
import sk.jacob.engine.Bus;

public class Boot {
    public static void main(String[] args) {
        Bus bus = new Bus(new JacobFirmware());
        bus.attach("HTTP_IN_OUT", new Jetty("core", "ui", "magua", 5558));
        bus.start();

        String json =
                "{'type': 'business.uom.add'," +
                " 'version': '0.1'," +
                " 'i': 1," +
                " 's': 'hello'}";

        String json2 =
                "{'type': 'business.uom.add'," +
                " 'version': '0.2'," +
                " 'i': 2," +
                " 's': 'world'}";
    }
}
