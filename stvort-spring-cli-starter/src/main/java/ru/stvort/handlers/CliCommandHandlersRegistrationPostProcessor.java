package ru.stvort.handlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import ru.stvort.annotations.CliController;
import ru.stvort.annotations.CliCommandMapping;

import java.util.Arrays;

/**
 * BeanPostProcessor for determine and register cli commands handlers methods
 * in beans marked with CliController annotation
 * Cli commands handler method must be marked with CliCommandMapping annotation
 *
 * @author Alexander Orudzhev
 *
 * @see org.springframework.beans.factory.config.BeanPostProcessor
 * @see ru.stvort.annotations.CliController
 * @see ru.stvort.annotations.CliCommandMapping
 * @see ru.stvort.handlers.CliCommandMetaData
 * @see ru.stvort.handlers.CliCommandHandlersRegistry
 */
public class CliCommandHandlersRegistrationPostProcessor implements BeanPostProcessor {

    private final Log logger = LogFactory.getLog(getClass());
    private final CliCommandHandlersRegistry registry;

    public CliCommandHandlersRegistrationPostProcessor(CliCommandHandlersRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (bean.getClass().isAnnotationPresent(CliController.class)) {

            Arrays.stream(bean.getClass().getDeclaredMethods())
                    .filter(m -> m.canAccess(bean))
                    .filter(m -> m.isAnnotationPresent(CliCommandMapping.class))
                    .forEach(m -> {
                        var commandMapping = m.getAnnotation(CliCommandMapping.class);
                        Arrays.stream(commandMapping.value())
                                .map(key -> new CliCommandMetaData(key, m, bean))
                                .forEach(registry::registerCommand);
                    });
        }
        return bean;
    }
}
