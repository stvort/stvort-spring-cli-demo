package ru.stvort.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import ru.stvort.handlers.CliCommandHandlersRegistrationPostProcessor;
import ru.stvort.io.*;
import ru.stvort.handlers.CliCommandsHandlerImpl;
import ru.stvort.menu.CommandLineParser;
import ru.stvort.menu.CommandLineParserImpl;
import ru.stvort.menu.MenuLoop;
import ru.stvort.menu.providers.MenuMessageProvider;
import ru.stvort.runner.CliRunner;

import java.util.List;

@ConditionalOnProperty(value = "stvort.cli.enabled", havingValue = "true")
@Configuration
public class CliAutoConfiguration {

    @ConditionalOnMissingBean(CliInputStreamProvider.class)
    @Bean
    public CliInputStreamProvider cliInputStreamProvider() {
        return new CliSystemInputStreamProviderImpl();
    }

    @ConditionalOnMissingBean(CliOutputStreamProvider.class)
    @Bean
    public CliOutputStreamProvider cliOutputStreamProvider() {
        return new CliSystemOutputStreamProvider();
    }

    @ConditionalOnMissingBean(CliInputOutputWorker.class)
    @Bean
    public CliInputOutputWorker cliInputOutputWorker() {
        return new CliInputOutputWorkerImpl(cliInputStreamProvider(), cliOutputStreamProvider());
    }

    @ConditionalOnMissingBean(CommandLineParser.class)
    @Bean
    public CommandLineParser commandLineParser() {
        return new CommandLineParserImpl();
    }

    @Bean
    public CliCommandsHandlerImpl cliCommandHandler() {
        return new CliCommandsHandlerImpl(commandLineParser());
    }

    @Bean
    public CliCommandHandlersRegistrationPostProcessor cliCommandHandlersRegistrationPostProcessor(){
        return new CliCommandHandlersRegistrationPostProcessor(cliCommandHandler());
    }

    @Bean
    public MenuLoop menuLoop(@Autowired(required = false) List<MenuMessageProvider> messageProviders) {
        return new MenuLoop(cliInputOutputWorker(),
                cliCommandHandler(),
                messageProviders == null? List.of(): messageProviders);
    }

    @Bean
    public CliRunner cliRunner(@Value("${stvort.cli.order:" + Ordered.LOWEST_PRECEDENCE + "}") int order,
                               MenuLoop menuLoop) {
        return new CliRunner(order, menuLoop);
    }
}
