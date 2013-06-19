package sk.jacob.engine.types;


import com.google.gson.JsonObject;

public class MessageType {
    public String rawRequest;
    public JsonObject jsonRequest;
    public RequestType request;
    public ResponseType response;
    public String rawResponse;

    public void createResponse(ResponseDataType responseData) {
        this.response = new ResponseType();
        this.response.resd = responseData;
        this.response.resh = new ResponseHeaderType();
        this.response.resh.status = "OK";
        this.response.resh.messageId = "12345678";
    }
}
