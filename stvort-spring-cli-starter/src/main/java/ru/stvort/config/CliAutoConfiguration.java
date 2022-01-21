package ru.stvort.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import ru.stvort.handlers.*;
import ru.stvort.handlers.transformers.ArgsListTransformer;
import ru.stvort.handlers.transformers.DefaultArgsListTransformer;
import ru.stvort.handlers.transformers.PlainTypeOneArgTransformer;
import ru.stvort.io.*;
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
    public PlainTypeOneArgTransformer plainTypeOneArgTransformer() {
        return new PlainTypeOneArgTransformer();
    }

    @Bean
    public DefaultArgsListTransformer defaultArgsListTransformer() {
        return new DefaultArgsListTransformer(plainTypeOneArgTransformer());
    }

    @Bean
    public CliCommandExecutor cliCommandExecutor(List<ArgsListTransformer> argsListTransformers) {
        return new CliCommandExecutor(plainTypeOneArgTransformer(), argsListTransformers);
    }

    @Bean
    public CliCommandsHandler cliCommandHandler(CliCommandExecutor cliCommandExecutor) {
        return new CliCommandsHandlerImpl(commandLineParser(), cliCommandExecutor);
    }

    @DependsOn("cliCommandHandler")
    @Bean
    public CliCommandHandlersRegistrationPostProcessor cliCommandHandlersRegistrationPostProcessor(CliCommandHandlersRegistry registry){
        return new CliCommandHandlersRegistrationPostProcessor(registry);
    }

    @Bean
    public MenuLoop menuLoop(@Autowired(required = false) List<MenuMessageProvider> messageProviders,
                             CliCommandsHandler cliCommandsHandler) {
        return new MenuLoop(cliInputOutputWorker(),
                cliCommandsHandler,
                messageProviders == null? List.of(): messageProviders);
    }

    @Bean
    public CliRunner cliRunner(@Value("${stvort.cli.order:" + Ordered.LOWEST_PRECEDENCE + "}") int order,
                               MenuLoop menuLoop) {
        return new CliRunner(order, menuLoop);
    }
}
