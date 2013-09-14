package sk.jacob.engine;

import sk.jacob.engine.handler.HandlerRegistry;

import java.lang.annotation.Annotation;
import java.util.List;

public abstract class ApplicationModule<T extends Annotation, K>
        extends HandlerRegistry<T, K>
        implements IApplicationModule {
    protected ApplicationModule(Class<T> supportedAnnotation,
                                Class<K> payloadSuperClass,
                                List<Class> messageHandlers) {
        super(supportedAnnotation, payloadSuperClass, messageHandlers);
    }
}
