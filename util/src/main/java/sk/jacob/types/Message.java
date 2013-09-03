package sk.jacob.types;


import com.google.gson.JsonObject;

public class Message {
    public String rawRequest;
    public String rawResponse;
    public Request request;
    public Response response;
    public transient JsonObject jsonRequest;
    public final transient ExecutionContext executionContext = new ExecutionContext();
}
