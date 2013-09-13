package sk.jacob.appcommon.types;

public class Request<RQD extends RequestData> {
    public RequestHeader reqh;
    public RQD reqd;
}