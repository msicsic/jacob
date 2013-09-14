package sk.jacob.engine;

import sk.jacob.engine.handler.HandlerRegistry;

import java.util.List;

public abstract class ApplicationModule extends HandlerRegistry implements IApplicationModule {
    protected ApplicationModule(Class supportedAnnotation, List<Class> messageHandlers) {
        super(supportedAnnotation, messageHandlers);
    }
}
