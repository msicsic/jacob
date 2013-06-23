package sk.jacob.engine.types;

public class RequestType<H extends RequestHeader, D extends RequestData> {
    public H reqh;

    public D reqd;
}