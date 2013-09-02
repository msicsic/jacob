package sk.jacob.mpu.business.devel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import sk.jacob.common.MESSAGE;
import sk.jacob.types.ExecutionContext;
import sk.jacob.mpu.business.BusinessModule;

public class MpuListTest {
    private BusinessModule module = new BusinessModule();

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void testMpuList() {
        ExecutionContext executionContext =  MESSAGE.createDataPacket("{type: 'devel.mpuList', version: '0.1'}");
        executionContext = module.handle(executionContext);
        System.out.println(gson.toJson(MESSAGE.current(executionContext).response));
    }
}
