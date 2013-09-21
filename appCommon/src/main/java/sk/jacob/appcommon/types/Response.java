package sk.jacob.appcommon.types;

public class Response<RSD extends ResponseData> {
    public ResponseHeader resh = new ResponseHeader();;
    public RSD resd;
}
