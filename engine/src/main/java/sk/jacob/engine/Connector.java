package sk.jacob.engine;

public interface Connector {
    void start();
    void stop();
    void init(String portId, Bus bus);
}
