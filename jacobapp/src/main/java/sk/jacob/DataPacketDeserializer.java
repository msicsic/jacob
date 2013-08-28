package sk.jacob;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import sk.jacob.common.MESSAGE;
import sk.jacob.engine.Module;
import sk.jacob.types.DataPacket;
import sk.jacob.types.RequestHeaderType;
import sk.jacob.types.RequestType;

public class DataPacketDeserializer implements Module {
    private static final JsonParser JSON_PARSER = new JsonParser();
    private static final Gson GSON = new Gson();

    @Override
    public DataPacket handle(DataPacket dataPacket) {
        MESSAGE.current(dataPacket).jsonRequest =
                JSON_PARSER.parse(MESSAGE.current(dataPacket).rawRequest).getAsJsonObject();
        MESSAGE.current(dataPacket).request = new RequestType();
        MESSAGE.current(dataPacket).request.reqh =
                GSON.fromJson(getRequestHeader(dataPacket), RequestHeaderType.class);
        return dataPacket;
    }

    private JsonObject getRequestHeader(DataPacket dataPacket) {
        JsonObject jsonRequest = MESSAGE.current(dataPacket).jsonRequest;
        return jsonRequest.get("reqh").getAsJsonObject();
    }
}
