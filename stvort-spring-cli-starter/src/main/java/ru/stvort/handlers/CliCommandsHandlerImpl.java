package ru.stvort.handlers;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.stvort.handlers.exceptions.*;
import ru.stvort.menu.CommandLineParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CliCommandsHandlerImpl implements CliCommandHandlersRegistry, CliCommandsHandler {

    private final Log logger = LogFactory.getLog(getClass());

    private final CommandLineParser parser;
    private final CliCommandExecutor cliCommandExecutor;
    private final Map<String, CliCommandMetaData> commandsMetaDataMap;

    public CliCommandsHandlerImpl(CommandLineParser parser, CliCommandExecutor cliCommandExecutor) {
        this.parser = parser;
        this.cliCommandExecutor = cliCommandExecutor;
        commandsMetaDataMap = new HashMap<>();
    }

    @Override
    public Object handle(String commandLine) {
        List<String> parts = parser.parse(commandLine);
        if (parts.isEmpty()) {
            throw new EmptyCliCommandException();
        }

        if (!commandsMetaDataMap.containsKey(parts.get(0))) {
            throw new CliCommandNotFoundException(String.format("Команда %s не зарегистрирована", parts.get(0)));
        }

        var metaData = commandsMetaDataMap.get(parts.get(0));
        List<String> commandArgs = (parts.size() == 1)? List.of(): parts.subList(1, parts.size());
        return cliCommandExecutor.invokeCommandByMetaData(metaData, commandArgs);
    }

    @Override
    public void registerCommand(CliCommandMetaData metaData) {
        if (commandsMetaDataMap.containsKey(metaData.getCommand())) {
            throw new CliDuplicateCommandException(String.format("Команда %s уже зарегистрирована",
                    metaData.getCommand()));
        }
        commandsMetaDataMap.put(metaData.getCommand(), metaData);
    }


}
