package sk.jacob.engine;

import sk.jacob.engine.handler.HandlerRegistry;

import java.util.List;

public abstract class ApplicationModule
        extends HandlerRegistry
        implements IApplicationModule {
    protected ApplicationModule(List<Class> messageHandlers) {
        super(messageHandlers);
    }
}
