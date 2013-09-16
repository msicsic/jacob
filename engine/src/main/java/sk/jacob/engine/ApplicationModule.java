package sk.jacob.engine;

import sk.jacob.engine.handler.HandlerRegistry;

import java.util.List;

public abstract class ApplicationModule
        extends HandlerRegistry
        implements IApplicationModule {
    protected ApplicationModule(Class<?> payloadSuperClass,
                                List<Class> messageHandlers) {
        super(payloadSuperClass, messageHandlers);
    }
}
