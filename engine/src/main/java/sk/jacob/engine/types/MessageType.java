package sk.jacob.engine.types;


import com.google.gson.JsonObject;

public class MessageType {
    public String rawRequest;
    public JsonObject jsonRequest;
    public RequestType request;
    public ResponseType response;
    public String rawResponse;
}
