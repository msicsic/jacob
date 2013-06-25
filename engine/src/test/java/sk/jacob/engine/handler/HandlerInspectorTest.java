package sk.jacob.engine.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import sk.jacob.engine.types.DataPacket;
import sk.jacob.engine.types.RequestType;
import sk.jacob.engine.types.ResponseType;
// FIXME:
public class HandlerInspectorTest {
    class TestMpu {
        public class User {
            String login;

            String name;

            String email;
        }

        public abstract class ReqSuper extends RequestType {
            @Required
            Long start;

            @Required
            Integer count;
        }

        public class Req extends ReqSuper {
            String[] groups;

            @Length(max = 255)
            String login;
        }

        public class Res extends ResponseType {
            Integer totalCount;

            List<User> users;
        }

//        @Message(type = "test",
//                version = "0.1",
//                reqd = Req.class,
//                resd = Res.class)
        public DataPacket method(DataPacket dataPacket) {
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

        System.out.println(gson.toJson(HandlerInspector.serializeMethod(handlerMethod)));
    }
}
