package sk.jacob;

import sk.jacob.connector.http.HttpConnector;
import sk.jacob.engine.Application;
import sk.jacob.engine.Bus;
import sk.jacob.engine.InPort;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Boot {
    public static void main(String[] args) {
        Properties config = loadConfig(args[0]);
        InPort defaultInPort = new InPort("DEFAULT_PORT");
        defaultInPort.attach(new HttpConnector(config));
        Application application = new JacobApplication(config);
        Bus bus = new Bus(application);
        bus.attach(defaultInPort);
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
