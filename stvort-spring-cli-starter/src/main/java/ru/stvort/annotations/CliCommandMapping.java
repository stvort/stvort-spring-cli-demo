package ru.stvort.annotations;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that an annotated method is a handler for command line interface command
 *
 * <p>This annotation is typically used in combination with annotated class based on the
 * {@link ru.stvort.annotations.CliController} annotation.
 *
 * @author Alexander Orudzhev
 * @see ru.stvort.annotations.CliController
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CliCommandMapping {
    /**
     * Command names or aliases that should be handled by annotated method
     * @return command names or aliases
     */
    String[] value();
}
