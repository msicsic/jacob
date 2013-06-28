package sk.jacob.engine.handler;

import sk.jacob.engine.handler.devel.NoneResponseDataType;
import sk.jacob.engine.types.ResponseDataType;
import sk.jacob.engine.types.ResponseType;
import sk.jacob.engine.types.TokenType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Token {
    String type();
    Class<? extends TokenType> token();
    Class<? extends ResponseDataType> resd() default NoneResponseDataType.class;
}
