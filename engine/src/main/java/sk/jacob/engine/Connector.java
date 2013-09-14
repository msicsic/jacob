package sk.jacob.engine;

public interface Connector {
    void start();
    void stop();
    void associateWith(InPort inPort);
}
