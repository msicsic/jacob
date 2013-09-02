package sk.jacob;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import sk.jacob.common.MESSAGE;
import sk.jacob.engine.Module;
import sk.jacob.types.ExecutionContext;
import sk.jacob.types.Request;
import sk.jacob.types.RequestHeader;

public class DataPacketDeserializer implements Module {
    private static final JsonParser JSON_PARSER = new JsonParser();
    private static final Gson GSON = new Gson();

    @Override
    public ExecutionContext handle(ExecutionContext executionContext) {
        MESSAGE.current(executionContext).jsonRequest =
                JSON_PARSER.parse(MESSAGE.current(executionContext).rawRequest).getAsJsonObject();
        MESSAGE.current(executionContext).request = new Request();
        MESSAGE.current(executionContext).request.reqh =
                GSON.fromJson(getRequestHeader(executionContext), RequestHeader.class);
        return executionContext;
    }

    private JsonObject getRequestHeader(ExecutionContext executionContext) {
        JsonObject jsonRequest = MESSAGE.current(executionContext).jsonRequest;
        return jsonRequest.get("reqh").getAsJsonObject();
    }
}
