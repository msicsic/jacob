package sk.jacob;

import com.google.gson.Gson;
import sk.jacob.appcommon.accessor.COMMON;
import sk.jacob.appcommon.types.Message;
import sk.jacob.engine.ApplicationModule;
import sk.jacob.appcommon.types.ExecutionContext;

public class MessageSerializer implements ApplicationModule {
    private Gson gson = new Gson();

    @Override
    public ExecutionContext handle(ExecutionContext ec) {
        Message msg = COMMON.MESSAGE.getFrom(ec);
        msg.rawResponse = this.gson.toJson(msg.response);
        return ec;
    }
}
