package sk.jacob.engine;

import sk.jacob.engine.handler.HandlerRegistry;

import java.util.List;

public abstract class ApplicationModule<PAYLOAD>
        extends HandlerRegistry<PAYLOAD>
        implements IApplicationModule {
    protected ApplicationModule(Class<PAYLOAD> payloadSuperClass,
                                List<Class> messageHandlers) {
        super(payloadSuperClass, messageHandlers);
    }
}
