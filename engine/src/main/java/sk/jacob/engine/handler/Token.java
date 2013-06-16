package sk.jacob.engine.handler;

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
    String version();
    Class<? extends TokenType> token();
    Class<? extends ResponseType> resd();
}
