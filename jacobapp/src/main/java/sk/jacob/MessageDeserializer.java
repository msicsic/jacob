package sk.jacob;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import sk.jacob.appcommon.accessor.COMMON;
import sk.jacob.appcommon.types.Message;
import sk.jacob.engine.IApplicationModule;
import sk.jacob.appcommon.types.ExecutionContext;
import sk.jacob.appcommon.types.Request;
import sk.jacob.appcommon.types.RequestHeader;

public class MessageDeserializer implements IApplicationModule {
    private static final JsonParser JSON_PARSER = new JsonParser();
    private static final Gson GSON = new Gson();

    @Override
    public ExecutionContext handle(ExecutionContext ec) {
        Message msg = COMMON.MESSAGE.getFrom(ec);
        msg.jsonRequest = JSON_PARSER.parse(msg.rawRequest).getAsJsonObject();
        msg.request = new Request();
        msg.request.reqh = GSON.fromJson(getRequestHeader(ec), RequestHeader.class);
        return ec;
    }

    private JsonObject getRequestHeader(ExecutionContext ec) {
        JsonObject jsonRequest = COMMON.MESSAGE.getFrom(ec).jsonRequest;
        return jsonRequest.get("reqh").getAsJsonObject();
    }
}
