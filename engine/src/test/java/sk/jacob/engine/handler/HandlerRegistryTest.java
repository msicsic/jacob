package sk.jacob.engine.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Ignore;
import org.junit.Test;
import sk.jacob.appcommon.annotation.Required;
import sk.jacob.appcommon.types.ExecutionContext;
import sk.jacob.appcommon.types.RequestData;
import sk.jacob.appcommon.types.ResponseData;

import java.lang.reflect.Method;
import java.util.List;

// FIXME:
public class HandlerRegistryTest {
    static class TestMpu {
        static class User {
            String login;
            String name;
            String email;
        }

        static abstract class ReqSuper extends RequestData {
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

        static class Res extends ResponseData {
            Integer totalCount;
            List<User> users;
        }

        @DataTypes(type = "test",
                version = "0.1",
                reqd = Req.class,
                resd = Res.class)
        public static ExecutionContext method(ExecutionContext ec) {
            return ec;
        }
    }
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Ignore
    @Test
    public void testSerializeMethod() throws Exception {
        TestMpu testMpu = new TestMpu();

        Class mpuClass = testMpu.getClass();
        Method handlerMethod = mpuClass.getMethod("method", ExecutionContext.class);

        System.out.println(gson.toJson(HandlerRegistry.serializeMethod(handlerMethod)));
    }
}
