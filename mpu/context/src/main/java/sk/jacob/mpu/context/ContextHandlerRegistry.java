package sk.jacob.mpu.context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import sk.jacob.appcommon.accessor.COMMON;
import sk.jacob.appcommon.accessor.SECURITY;
import sk.jacob.appcommon.types.*;
import sk.jacob.engine.handler.DataTypes;
import sk.jacob.engine.handler.HandlerRegistry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class ContextHandlerRegistry extends HandlerRegistry<DataTypes> {
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
    protected void processRequestClass(ExecutionContext ec, Method handler) {
        JsonObject jsonRequest = COMMON.MESSAGE.getFrom(ec).jsonRequest;
        Request request = new Request();

        request.reqh = new Gson().fromJson(jsonRequest.get("reqh"), RequestHeader.class);
        for (Class<?> clazz : handler.getParameterTypes()) {
            if (clazz.getSuperclass().equals(RequestData.class)) {
                request.reqd = (RequestData)new Gson().fromJson(jsonRequest.get("reqd"),clazz);
                COMMON.MESSAGE.getFrom(ec).request = request;
                break;
            }
        }
    }
}