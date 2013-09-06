package sk.jacob.appcommon.types;


import com.google.gson.JsonObject;

public class Message {
    public String rawRequest;
    public String rawResponse;
    public Request request;
    public Response response;
    public JsonObject jsonRequest;
}
