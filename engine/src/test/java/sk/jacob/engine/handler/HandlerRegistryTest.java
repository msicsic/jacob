package sk.jacob.engine.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;
import sk.jacob.annotation.Required;
import sk.jacob.types.DataPacket;
import sk.jacob.types.RequestDataType;
import sk.jacob.types.ResponseDataType;

// FIXME:
public class HandlerRegistryTest {
    static class TestMpu {
        static class User {
            String login;
            String name;
            String email;
        }

        static abstract class ReqSuper extends RequestDataType {
            @Required
            Long start;
            @Required
            Integer count;
        }

        static class Req extends ReqSuper {
            String[] groups;
            @Length(max = 255)
            String login;
        }

        static class Res extends ResponseDataType {
            Integer totalCount;
            List<User> users;
        }

        @Message(type = "test",
                version = "0.1",
                reqd = Req.class,
                resd = Res.class)
        public static DataPacket method(DataPacket dataPacket) {
            return dataPacket;
        }
    }
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Ignore
    @Test
    public void testSerializeMethod() throws Exception {
        TestMpu testMpu = new TestMpu();

        Class mpuClass = testMpu.getClass();
        Method handlerMethod = mpuClass.getMethod("method", DataPacket.class);

        System.out.println(gson.toJson(HandlerRegistry.serializeMethod(handlerMethod)));
    }
}