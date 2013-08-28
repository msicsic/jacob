package sk.jacob;

import com.google.gson.Gson;
import sk.jacob.common.MESSAGE;
import sk.jacob.engine.Module;
import sk.jacob.types.DataPacket;

public class DataPacketSerializer implements Module {
    private Gson gson = new Gson();

    @Override
    public DataPacket handle(DataPacket dataPacket) {
        MESSAGE.current(dataPacket).rawResponse = this.gson.toJson(MESSAGE.current(dataPacket).response);
        return dataPacket;
    }
}
