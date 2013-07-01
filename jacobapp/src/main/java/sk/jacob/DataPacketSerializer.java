package sk.jacob;

import com.google.gson.Gson;
import sk.jacob.engine.Module;
import sk.jacob.engine.types.DataPacket;

public class DataPacketSerializer implements Module {
    private Gson gson = new Gson();

    @Override
    public DataPacket handle(DataPacket dataPacket) {
        dataPacket.message.rawResponse = this.gson.toJson(dataPacket.message.response);
        return dataPacket;
    }
}
