package ru.stvort.handlers.transformers;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomArg {
    Class<? extends ArgsListTransformer> transformer() default DefaultArgsListTransformer.class;

    @AliasFor("transformer")
    Class<? extends ArgsListTransformer> value() default DefaultArgsListTransformer.class;
}
