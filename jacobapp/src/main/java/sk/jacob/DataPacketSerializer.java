package sk.jacob;

import com.google.gson.Gson;
import sk.jacob.common.MESSAGE;
import sk.jacob.engine.Module;
import sk.jacob.types.ExecutionContext;

public class DataPacketSerializer implements Module {
    private Gson gson = new Gson();

    @Override
    public ExecutionContext handle(ExecutionContext executionContext) {
        MESSAGE.current(executionContext).rawResponse = this.gson.toJson(MESSAGE.current(executionContext).response);
        return executionContext;
    }
}
