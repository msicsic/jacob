package sk.jacob.appcommon.types;


import com.google.gson.JsonObject;

public class Message<RQD extends RequestData, RSD extends ResponseData> {
    public String rawRequest;
    public String rawResponse;
    public Request<RQD> request;
    public Response<RSD> response;
    public JsonObject jsonRequest;
}
