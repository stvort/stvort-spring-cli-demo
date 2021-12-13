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
    private final Map<String, CliCommandMetaData> commandsMetaDataMap;

    public CliCommandsHandlerImpl(CommandLineParser parser) {
        this.parser = parser;
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
        return invokeCommand(metaData, (parts.size() == 1)? List.of(): parts.subList(1, parts.size()));
    }

    @Override
    public void registerCommand(CliCommandMetaData metaData) {
        if (commandsMetaDataMap.containsKey(metaData.getCommand())) {
            throw new CliDuplicateCommandException(String.format("Команда %s уже зарегистрирована",
                    metaData.getCommand()));
        }
        commandsMetaDataMap.put(metaData.getCommand(), metaData);
    }

    private Object invokeCommand(CliCommandMetaData metaData, List<String> args) {
        var commandArgs = prepareCommandArgsValues(metaData, args);
        try {
            return metaData.getCommandMethod().invoke(metaData.getBeanInstance(), commandArgs);
        } catch (Exception e) {
            throw new CliCommandInvocationException(String.format("Ошибка в процессе выполнения команды %s",
                    metaData.getCommand()));
        }
    }

    private Object[] prepareCommandArgsValues(CliCommandMetaData metaData, List<String> args) {
        // TODO: Заменить хардкод на список конвертеров
        var paramsTypes = metaData.getCommandMethod().getParameterTypes();
        if (paramsTypes.length > args.size()) {
            throw new IllegalArgumentException("Передано недостаточное количество аргументов");
        }

        var commandArgs = new Object[paramsTypes.length];
        for (int i = 0; i < paramsTypes.length; i++) {
            if (paramsTypes[i].equals(String.class)) {
                commandArgs[i] = args.get(i);
            } else if (paramsTypes[i].equals(Integer.class) || paramsTypes[i].equals(int.class)) {
                commandArgs[i] = Integer.valueOf(args.get(i));
            } else {
                throw new UnsupportedArgumentTypeException(String.format("Аргументы типа %s не поддерживаются",
                        paramsTypes[i]));
            }
        }
        return commandArgs;
    }
}
