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
    public ExecutionContext handle(ExecutionContext ec) {
        MESSAGE.get(ec).jsonRequest =
                JSON_PARSER.parse(MESSAGE.get(ec).rawRequest).getAsJsonObject();
        MESSAGE.get(ec).request = new Request();
        MESSAGE.get(ec).request.reqh =
                GSON.fromJson(getRequestHeader(ec), RequestHeader.class);
        return ec;
    }

    private JsonObject getRequestHeader(ExecutionContext ec) {
        JsonObject jsonRequest = MESSAGE.get(ec).jsonRequest;
        return jsonRequest.get("reqh").getAsJsonObject();
    }
}
