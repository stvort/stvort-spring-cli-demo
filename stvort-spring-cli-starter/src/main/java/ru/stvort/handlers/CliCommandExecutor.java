package ru.stvort.handlers;

import ru.stvort.handlers.exceptions.CliCommandInvocationException;
import ru.stvort.handlers.exceptions.UnsupportedArgumentTypeException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Class contains logic for execution cli commands handlers by
 * given metadata {@link ru.stvort.handlers.CliCommandMetaData} and args list
 *
 * @author Alexander Orudzhev
 *
 * @see ru.stvort.handlers.CliCommandMetaData
 */
public class CliCommandExecutor {
    private final Map<Class<?>, Function<String, Object>> defaultTransformers;

    public CliCommandExecutor() {
        defaultTransformers = new HashMap<>();
        initDefaultTransformers();
    }

    /**
     * Executed cli commands handlers by given metadata and args list
     * @param metaData - metadata of command to execute
     * @param args - command line args that needed to command execution
     * @return result of command execution or {@code null} if handler has {@code void} return type
     * @throws CliCommandInvocationException thant wrapped any exceptions during execution
     */
    public Object invokeCommandByMetaData(CliCommandMetaData metaData, List<String> args) {
        var commandArgs = prepareCommandArgsValues(metaData, args);
        try {
            return metaData.getCommandMethod().invoke(metaData.getBeanInstance(), commandArgs);
        } catch (Exception e) {
            throw new CliCommandInvocationException(String.format("Ошибка в процессе выполнения команды %s",
                    metaData.getCommand()), e);
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
        defaultTransformers.put(byte.class, Byte::valueOf);
        defaultTransformers.put(short.class, Short::valueOf);
        defaultTransformers.put(int.class, Integer::valueOf);
        defaultTransformers.put(long.class, Long::valueOf);
        defaultTransformers.put(boolean.class, Boolean::valueOf);

        defaultTransformers.put(Byte.class, Byte::valueOf);
        defaultTransformers.put(Short.class, Short::valueOf);
        defaultTransformers.put(Integer.class, Integer::valueOf);
        defaultTransformers.put(Long.class, Long::valueOf);
        defaultTransformers.put(Boolean.class, Boolean::valueOf);

    }
}
