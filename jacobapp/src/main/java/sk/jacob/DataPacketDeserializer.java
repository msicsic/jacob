package sk.jacob;

import com.google.gson.JsonParser;
import sk.jacob.engine.Module;
import sk.jacob.engine.types.DataPacket;

public class DataPacketDeserializer implements Module {
    private static final JsonParser JSON_PARSER = new JsonParser();

    @Override
    public DataPacket handle(DataPacket dataPacket) {
        dataPacket.message.jsonRequest = JSON_PARSER.parse(dataPacket.message.rawRequest).getAsJsonObject();
        return dataPacket;
    }
}
