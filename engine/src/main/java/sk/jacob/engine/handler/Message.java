package sk.jacob.engine.handler;

import sk.jacob.types.RequestData;
import sk.jacob.types.ResponseData;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Message {
    String type();
    String version();
    Class<? extends RequestData> reqd();
    Class<? extends ResponseData> resd();
}
