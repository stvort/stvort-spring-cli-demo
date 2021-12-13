package ru.stvort.handlers;

public interface CliCommandHandlersRegistry {
    void registerCommand(CliCommandMetaData metaData);
}
