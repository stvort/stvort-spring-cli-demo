package ru.stvort.handlers.transformers;

import ru.stvort.handlers.CliCommandArgs;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class DefaultArgsListTransformer implements ArgsListTransformer {

    private final PlainTypeOneArgTransformer plainTypeOneArgTransformer;

    public DefaultArgsListTransformer(PlainTypeOneArgTransformer plainTypeOneArgTransformer) {
        this.plainTypeOneArgTransformer = plainTypeOneArgTransformer;
    }

    @Override
    public Object transform(CliCommandArgs args, Class<?> targetClass) {
        var res = getClassInstance(targetClass);
        setUpFieldsValues(args, res);

        return res;
    }

    private void setUpFieldsValues(CliCommandArgs args, Object target) {
        for (Field f : target.getClass().getDeclaredFields()) {
            var value = args.getNamedArg(f.getName().toLowerCase());
            if (value == null) {
                continue;
            }

            f.setAccessible(true);
            try {
                f.set(target, plainTypeOneArgTransformer.transform(value, f.getType()));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(String.format("Не удалось выставить значение поля %s", f.getName()), e);
            }

        }
    }

    private Object getClassInstance(Class<?> targetClass) {
        try {
            return targetClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Не удалось создать целевой класс с помощью конструктора по умолчанию", e);
        }
    }
}
