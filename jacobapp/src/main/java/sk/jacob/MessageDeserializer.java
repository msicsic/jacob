package sk.jacob;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import sk.jacob.appcommon.accessor.COMMON;
import sk.jacob.engine.Module;
import sk.jacob.appcommon.types.ExecutionContext;
import sk.jacob.appcommon.types.Request;
import sk.jacob.appcommon.types.RequestHeader;

public class MessageDeserializer implements Module {
    private static final JsonParser JSON_PARSER = new JsonParser();
    private static final Gson GSON = new Gson();

    @Override
    public ExecutionContext handle(ExecutionContext ec) {
        COMMON.getMessage(ec).jsonRequest =
                JSON_PARSER.parse(COMMON.getMessage(ec).rawRequest).getAsJsonObject();
        COMMON.getMessage(ec).request = new Request();
        COMMON.getMessage(ec).request.reqh =
                GSON.fromJson(getRequestHeader(ec), RequestHeader.class);
        return ec;
    }

    private JsonObject getRequestHeader(ExecutionContext ec) {
        JsonObject jsonRequest = COMMON.getMessage(ec).jsonRequest;
        return jsonRequest.get("reqh").getAsJsonObject();
    }
}
