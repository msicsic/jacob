package sk.jacob.engine.handler;

import sk.jacob.engine.handler.devel.NoneResponseData;
import sk.jacob.types.ResponseData;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface TokenTypes {
    String type();
    Class<? extends sk.jacob.types.Token> token();
    Class<? extends ResponseData> resd() default NoneResponseData.class;
}
