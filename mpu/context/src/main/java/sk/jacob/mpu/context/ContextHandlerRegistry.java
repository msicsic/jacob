package sk.jacob.mpu.context;

import com.google.gson.Gson;
import java.lang.annotation.Annotation;
import java.util.List;

import com.google.gson.JsonObject;
import sk.jacob.common.MESSAGE;
import sk.jacob.engine.handler.DataTypes;
import sk.jacob.engine.handler.HandlerRegistry;
import sk.jacob.types.ExecutionContext;
import sk.jacob.types.Request;
import sk.jacob.types.RequestHeader;

public class ContextHandlerRegistry extends HandlerRegistry<DataTypes> {
    public ContextHandlerRegistry(List<Class> messageHandlers) {
        super(DataTypes.class, messageHandlers);
    }

    @Override
    protected String getHandlerKey(DataTypes annotation) {
        return annotation.type();
    }

    @Override
    protected String getMessageType(ExecutionContext ec) {
        return MESSAGE.get(ec).request.reqh.type;
    }

    @Override
    protected void deserializeMessageElement(ExecutionContext ec, Annotation annotation) {
        DataTypes dataTypes = (DataTypes)annotation;
        JsonObject jsonRequest = MESSAGE.get(ec).jsonRequest;
        Request request = new Request();

        request.reqh = new Gson().fromJson(jsonRequest.get("reqh"), RequestHeader.class);
        request.reqd = new Gson().fromJson(jsonRequest.get("reqd"), dataTypes.reqd());

        MESSAGE.get(ec).request = request;
    }
}