package sk.jacob;

import com.google.gson.Gson;
import sk.jacob.appcommon.accessor.COMMON;
import sk.jacob.appcommon.types.Message;
import sk.jacob.engine.IApplicationModule;
import sk.jacob.appcommon.types.ExecutionContext;

public class MessageSerializer implements IApplicationModule {
    private Gson gson = new Gson();

    @Override
    public ExecutionContext onRequest(ExecutionContext ec) {
        Message msg = COMMON.MESSAGE.getFrom(ec);
        msg.rawResponse = this.gson.toJson(msg.response);
        return ec;
    }
}
