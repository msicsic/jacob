package sk.jacob.mpu.business.devel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import sk.jacob.common.MESSAGE;
import sk.jacob.types.DataPacket;
import sk.jacob.mpu.business.BusinessModule;

public class MpuListTest {
    private BusinessModule module = new BusinessModule();

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void testMpuList() {
        DataPacket dataPacket =  MESSAGE.createDataPacket("{type: 'devel.mpuList', version: '0.1'}");
        dataPacket = module.handle(dataPacket);
        System.out.println(gson.toJson(MESSAGE.current(dataPacket).response));
    }
}
