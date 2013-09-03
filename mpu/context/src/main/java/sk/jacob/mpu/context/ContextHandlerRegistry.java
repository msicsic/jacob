package sk.jacob.mpu.context;

import com.google.gson.Gson;
import java.lang.annotation.Annotation;
import java.util.List;

import com.google.gson.JsonObject;
import sk.jacob.common.MESSAGE;
import sk.jacob.engine.handler.HandlerRegistry;
import sk.jacob.engine.handler.Signature;
import sk.jacob.types.ExecutionContext;
import sk.jacob.types.Request;
import sk.jacob.types.RequestHeader;

public class ContextHandlerRegistry extends HandlerRegistry<Signature> {
    public ContextHandlerRegistry(List<Class> messageHandlers) {
        super(Signature.class, messageHandlers);
    }

    @Override
    protected String getHandlerKey(Signature annotation) {
        return annotation.type();
    }

    @Override
    protected String getMessageType(ExecutionContext executionContext) {
        return MESSAGE.current(executionContext).request.reqh.type;
    }

    @Override
    protected void deserializeMessageElement(ExecutionContext executionContext, Annotation annotation) {
        Signature message = (Signature) annotation;
        JsonObject jsonRequest = MESSAGE.current(executionContext).jsonRequest;
        Request request = new Request();

        request.reqh = new Gson().fromJson(jsonRequest.get("reqh"), RequestHeader.class);
        request.reqd = new Gson().fromJson(jsonRequest.get("reqd"), message.reqd());

        MESSAGE.current(executionContext).request = request;
    }
}