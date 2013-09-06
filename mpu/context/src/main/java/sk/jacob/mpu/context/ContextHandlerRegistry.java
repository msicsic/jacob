package sk.jacob.mpu.context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import sk.jacob.appcommon.accessor.COMMON;
import sk.jacob.engine.handler.DataTypes;
import sk.jacob.engine.handler.HandlerRegistry;
import sk.jacob.appcommon.types.ExecutionContext;
import sk.jacob.appcommon.types.Request;
import sk.jacob.appcommon.types.RequestHeader;

import java.lang.annotation.Annotation;
import java.util.List;

public class ContextHandlerRegistry extends HandlerRegistry<DataTypes> {
    public ContextHandlerRegistry(List<Class> messageHandlers) {
        super(DataTypes.class, messageHandlers);
    }

    @Override
    protected String getHandlerKey(DataTypes annotation) {
        return annotation.type() + "." + annotation.version();
    }

    @Override
    protected String getMessageType(ExecutionContext ec) {
        RequestHeader rh = COMMON.getMessage(ec).request.reqh;
        return rh.type + "." + rh.version;
    }

    @Override
    protected void deserializeMessageElement(ExecutionContext ec, Annotation annotation) {
        DataTypes dataTypes = (DataTypes)annotation;
        JsonObject jsonRequest = COMMON.getMessage(ec).jsonRequest;
        Request request = new Request();

        request.reqh = new Gson().fromJson(jsonRequest.get("reqh"), RequestHeader.class);
        request.reqd = new Gson().fromJson(jsonRequest.get("reqd"), dataTypes.reqd());

        COMMON.getMessage(ec).request = request;
    }
}