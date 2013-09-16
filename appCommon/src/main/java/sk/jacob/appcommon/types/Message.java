package sk.jacob.appcommon.types;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Message<RQD extends RequestData, RSD extends ResponseData> {
    private static final JsonParser JSON_PARSER = new JsonParser();
    private static final Gson GSON = new Gson();

    public final String rawRequest;
    public final JsonObject jsonRequest;
    public final Request<RQD> request = new Request();
    public final Response<RSD> response = new Response<>();

    private Message(String rawRequest) {
        this.rawRequest = rawRequest;
        this.jsonRequest = JSON_PARSER.parse(this.rawRequest).getAsJsonObject();
        this.request.reqh = GSON.fromJson(this.jsonRequest.get("reqh"), RequestHeader.class);
    }

    public String getRawResponse() {
        return GSON.toJson(response);
    }

    public static Message getInstance(String rawRequest) {
        return new Message(rawRequest);
    }
}
