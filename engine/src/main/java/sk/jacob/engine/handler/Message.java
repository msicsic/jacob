package sk.jacob.engine.handler;

import sk.jacob.types.RequestDataType;
import sk.jacob.types.ResponseDataType;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Message {
    String type();
    String version();
    Class<? extends RequestDataType> reqd();
    Class<? extends ResponseDataType> resd();
}
