package sk.jacob;

import com.google.gson.Gson;
import sk.jacob.accessor.COMMON;
import sk.jacob.engine.Module;
import sk.jacob.types.ExecutionContext;

public class MessageSerializer implements Module {
    private Gson gson = new Gson();

    @Override
    public ExecutionContext handle(ExecutionContext ec) {
        COMMON.getMessage(ec).rawResponse = this.gson.toJson(COMMON.getMessage(ec).response);
        return ec;
    }
}
