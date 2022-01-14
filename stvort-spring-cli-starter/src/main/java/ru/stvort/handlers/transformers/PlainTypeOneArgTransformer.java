package ru.stvort.handlers.transformers;

import ru.stvort.handlers.exceptions.UnsupportedArgumentTypeException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PlainTypeOneArgTransformer {
    private final Map<Class<?>, Function<String, Object>> defaultTransformers;

    public PlainTypeOneArgTransformer() {
        defaultTransformers = initDefaultTransformers();
    }


    public Object transform(String arg, Class<?> targetClass) {
        if (defaultTransformers.containsKey(targetClass)) {
            return defaultTransformers.get(targetClass).apply(arg);
        }
        throw new UnsupportedArgumentTypeException(String.format("Аргументы типа %s не поддерживаются", targetClass));
    }

    public boolean canTransformTo(Class<?> targetClass) {
        return defaultTransformers.containsKey(targetClass);
    }

    private Map<Class<?>, Function<String, Object>> initDefaultTransformers() {
        var transformers = new HashMap<Class<?>, Function<String, Object>>();
        transformers.put(String.class, String::valueOf);
        transformers.put(byte.class, Byte::valueOf);
        transformers.put(short.class, Short::valueOf);
        transformers.put(int.class, Integer::valueOf);
        transformers.put(long.class, Long::valueOf);
        transformers.put(boolean.class, Boolean::valueOf);

        transformers.put(Byte.class, Byte::valueOf);
        transformers.put(Short.class, Short::valueOf);
        transformers.put(Integer.class, Integer::valueOf);
        transformers.put(Long.class, Long::valueOf);
        transformers.put(Boolean.class, Boolean::valueOf);
        return transformers;
    }

}
