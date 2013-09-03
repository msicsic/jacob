package sk.jacob.engine.handler;

import sk.jacob.types.RequestData;
import sk.jacob.types.ResponseData;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DataTypes {
    String type();
    String version();
    Class<? extends RequestData> reqd();
    Class<? extends ResponseData> resd();
}
