package sk.jacob.mpu.context;

import com.google.gson.JsonObject;
import sk.jacob.appcommon.accessor.COMMON;
import sk.jacob.appcommon.types.*;
import sk.jacob.engine.handler.DataTypes;
import sk.jacob.engine.handler.HandlerRegistry;

import java.util.List;

public class ContextHandlerRegistry extends HandlerRegistry<DataTypes, RequestData> {
    public ContextHandlerRegistry(List<Class> messageHandlers) {
        super(DataTypes.class, messageHandlers);
    }

    @Override
    protected String getHandlerType(DataTypes annotation) {
        return annotation.type() + "." + annotation.version();
    }

    @Override
    protected String getMessageType(ExecutionContext ec) {
        RequestHeader rh = COMMON.MESSAGE.getFrom(ec).request.reqh;
        return rh.type + "." + rh.version;
    }

    @Override
    protected void processRequestClass(ExecutionContext ec, Class<RequestData> payloadClass) {
        JsonObject jsonRequest = COMMON.MESSAGE.getFrom(ec).jsonRequest;
        Request request = new Request();
        request.reqh = GSON.fromJson(jsonRequest.get("reqh"), RequestHeader.class);
        request.reqd = GSON.fromJson(jsonRequest.get("reqd"), payloadClass);
        COMMON.MESSAGE.getFrom(ec).request = request;
    }
}