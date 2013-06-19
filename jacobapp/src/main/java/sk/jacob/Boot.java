package sk.jacob;

import sk.jacob.connector.http.Jetty;
import sk.jacob.engine.Bus;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Boot {
    public static void main(String[] args) {
        Properties config = loadConfig(args[0]);

        Bus bus = new Bus(new JacobFirmware(config));
        bus.attach("HTTP_IN_OUT", new Jetty(config));
        bus.start();
    }

    private static Properties loadConfig(String path) {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return p;
    }
}
