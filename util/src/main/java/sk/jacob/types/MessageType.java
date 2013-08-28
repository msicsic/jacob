package sk.jacob.types;


import com.google.gson.JsonObject;

public class MessageType {
    public String rawRequest;
    public String rawResponse;
    public RequestType request;
    public ResponseType response;
    public JsonObject jsonRequest;
}
