package sk.jacob.engine.handler;

import sk.jacob.engine.types.RequestType;
import sk.jacob.engine.types.ResponseType;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Message {
    String type();
    Class<? extends RequestType> reqd();
    Class<? extends ResponseType> resd();
    String version();
}
