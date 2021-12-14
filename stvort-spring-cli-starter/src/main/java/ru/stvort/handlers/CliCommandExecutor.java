package ru.stvort.handlers;

import ru.stvort.handlers.exceptions.CliCommandInvocationException;
import ru.stvort.handlers.exceptions.UnsupportedArgumentTypeException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CliCommandExecutor {
    private final Map<Class<?>, Function<String, Object>> defaultTransformers;

    public CliCommandExecutor() {
        defaultTransformers = new HashMap<>();
        initDefaultTransformers();
    }

    public Object invokeCommandByMetaData(CliCommandMetaData metaData, List<String> args) {
        var commandArgs = prepareCommandArgsValues(metaData, args);
        try {
            return metaData.getCommandMethod().invoke(metaData.getBeanInstance(), commandArgs);
        } catch (Exception e) {
            throw new CliCommandInvocationException(String.format("Ошибка в процессе выполнения команды %s",
                    metaData.getCommand()));
        }
    }

    private Object[] prepareCommandArgsValues(CliCommandMetaData metaData, List<String> args) {

        var paramsTypes = metaData.getCommandMethod().getParameterTypes();
        if (paramsTypes.length > args.size()) {
            throw new IllegalArgumentException("Передано недостаточное количество аргументов");
        }

        var commandArgs = new Object[paramsTypes.length];
        for (int i = 0; i < paramsTypes.length; i++) {
            if (defaultTransformers.containsKey(paramsTypes[i])) {
                commandArgs[i] = defaultTransformers.get(paramsTypes[i]).apply(args.get(i));
            } else {
                throw new UnsupportedArgumentTypeException(String.format("Аргументы типа %s не поддерживаются",
                        paramsTypes[i]));
            }
        }
        return commandArgs;
    }

    private void initDefaultTransformers(){
        defaultTransformers.put(String.class, String::valueOf);
        defaultTransformers.put(byte.class, Integer::valueOf);
        defaultTransformers.put(short.class, Integer::valueOf);
        defaultTransformers.put(int.class, Integer::valueOf);
        defaultTransformers.put(long.class, Integer::valueOf);
        defaultTransformers.put(Byte.class, Integer::valueOf);
        defaultTransformers.put(Short.class, Integer::valueOf);
        defaultTransformers.put(Integer.class, Integer::valueOf);
        defaultTransformers.put(Long.class, Integer::valueOf);
    }
}
