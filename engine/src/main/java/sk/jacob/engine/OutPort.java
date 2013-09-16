package sk.jacob.engine;

public class OutPort implements Port {
    public final String id;

    public OutPort(String id) {
        this.id = id;
    }

    @Override
    public void start() {
        //pass
    }
}
