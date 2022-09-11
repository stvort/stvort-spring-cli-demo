package ru.stvort.handlers;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.stvort.handlers.exceptions.*;
import ru.stvort.menu.CommandLineParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class containing logic for processing commands represented as a string
 * That class transform command line to command metadata and args list with
 * {@link ru.stvort.menu.CommandLineParser} and then dispatch command to it
 * handlers endpoints using {@link ru.stvort.handlers.CliCommandExecutor}
 *
 * @author Alxander Orudzhev
 *
 * @see ru.stvort.handlers.CliCommandHandlersRegistry
 * @see ru.stvort.handlers.CliCommandsHandler
 * @see ru.stvort.menu.CommandLineParser
 * @see ru.stvort.handlers.CliCommandExecutor
 * @see ru.stvort.handlers.CliCommandMetaData
 */
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

    /**
     * Handle (execute) given command and return it result if necessary
     * @param commandLine - command line represented as a string
     * @return result of command execution or {@code null} if command does not suggest a result
     * @throws EmptyCliCommandException - if given command line is empty
     * @throws CliCommandNotFoundException - if metadata for given command was not registered
     */
    @Override
    public Object handle(String commandLine) {
        var commandLineParts = parser.parse(commandLine);
        checkCommandLinePartsSize(commandLineParts);

        var commandName = extractCommandName(commandLineParts);
        checkCommandRegistered(commandName);

        var metaData = commandsMetaDataMap.get(commandName);
        var commandArgs = extractCommandArgs(commandLineParts);
        return cliCommandExecutor.invokeCommandByMetaData(metaData, commandArgs);
    }

    /**
     * Register metadata of command in {@code commandsMetaDataMap}
     * @param metaData - command line commands metadata to register
     * @throws CliDuplicateCommandException if command with given alias already registered
     */
    @Override
    public void registerCommand(CliCommandMetaData metaData) {
        if (commandsMetaDataMap.containsKey(metaData.getCommand())) {
            throw new CliDuplicateCommandException(String.format("Команда %s уже зарегистрирована",
                    metaData.getCommand()));
        }
        commandsMetaDataMap.put(metaData.getCommand(), metaData);
    }

    private void checkCommandLinePartsSize(List<String> commandLineParts){
        if (commandLineParts.isEmpty()) {
            throw new EmptyCliCommandException();
        }
    }

    private String extractCommandName(List<String> commandLineParts){
        return commandLineParts.get(0);
    }

    private void checkCommandRegistered(String commandName){
        if (!commandsMetaDataMap.containsKey(commandName)) {
            throw new CliCommandNotFoundException(String.format("Команда %s не зарегистрирована", commandName));
        }
    }

    private List<String> extractCommandArgs(List<String> commandLineParts){
        return (commandLineParts.size() == 1)?
                new ArrayList<>() :
                commandLineParts.subList(1, commandLineParts.size());
    }

}
