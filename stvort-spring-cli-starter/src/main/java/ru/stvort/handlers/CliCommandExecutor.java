package ru.stvort.handlers;

import ru.stvort.handlers.exceptions.CliCommandInvocationException;
import ru.stvort.handlers.exceptions.TransformerNotFoundException;
import ru.stvort.handlers.transformers.ArgsListTransformer;
import ru.stvort.handlers.transformers.CustomArg;
import ru.stvort.handlers.transformers.PlainTypeOneArgTransformer;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Class contains logic for execution cli commands handlers by
 * given metadata {@link ru.stvort.handlers.CliCommandMetaData} and args list
 *
 * @author Alexander Orudzhev
 * @see ru.stvort.handlers.CliCommandMetaData
 */
public class CliCommandExecutor {
    private final PlainTypeOneArgTransformer plainTypeOneArgTransformer;
    private final Map<Class<?>, ArgsListTransformer> argsListTransformers;

    public CliCommandExecutor(PlainTypeOneArgTransformer plainTypeOneArgTransformer,
                              List<ArgsListTransformer> argsListTransformers) {
        this.plainTypeOneArgTransformer = plainTypeOneArgTransformer;
        this.argsListTransformers = argsListTransformers.stream()
                .collect(Collectors.toMap(Object::getClass, Function.identity()));
    }

    /**
     * Executed cli commands handlers by given metadata and args list
     *
     * @param metaData - metadata of command to execute
     * @param args     - command line args that needed to command execution
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

    private Object[] prepareCommandArgsValues(CliCommandMetaData metaData, List<String> argsValues) {

        var argsValuesWrapper = CliCommandArgs.of(argsValues);
        var params = metaData.getCommandMethod().getParameters();
        checkPlainTypeArgsCount(params, argsValues.size());


        var commandMethodArgs = new Object[params.length];
        int lastUnNamedArgsIndex = 0;
        for (int i = 0; i < params.length; i++) {
            if (plainTypeOneArgTransformer.canTransformTo(params[i].getType())) {
                if (argsValuesWrapper.containsNamedArg(params[i].getName())) {
                    commandMethodArgs[i] = argsValuesWrapper.getNamedArg(params[i].getName());
                    continue;
                }
                commandMethodArgs[i] = plainTypeOneArgTransformer.transform(
                        argsValuesWrapper.getUnNamedArgByIndex(lastUnNamedArgsIndex++), params[i].getType());

            } else {
                commandMethodArgs[i] = getCustomArgValue(params[i], argsValuesWrapper);
            }
        }
        return commandMethodArgs;
    }

    private void checkPlainTypeArgsCount(Parameter[] params, int realArgsCount) {
        var plainTypeArgsCount = Arrays.stream(params)
                .map(Parameter::getType)
                .filter(plainTypeOneArgTransformer::canTransformTo)
                .count();
        if (plainTypeArgsCount > realArgsCount) {
            throw new IllegalArgumentException("Передано недостаточное количество аргументов");
        }
    }

     private Object getCustomArgValue(Parameter parameter, CliCommandArgs argsValues) {
        var customArgAnnotation = parameter.getAnnotation(CustomArg.class);

        if (customArgAnnotation != null) {
            var transformerClass = customArgAnnotation.transformer();
            if (!argsListTransformers.containsKey(transformerClass)) {
                throw new TransformerNotFoundException(String.format("Трансформер %s отсутствует в контексте",
                        transformerClass));
            }
            return argsListTransformers.get(transformerClass).transform(argsValues, parameter.getType());
        } else {
            throw new TransformerNotFoundException(String.format("Аргумент %s не является простым " +
                    "типом и не отмечен аннотацией @CustomArg", parameter.getName()));
        }
    }
}
