package ru.stvort.handlers.transformers;

import ru.stvort.handlers.CliCommandArgs;

import java.util.List;

public interface ArgsListTransformer {
    Object transform(CliCommandArgs args, Class<?> targetClass);
}
