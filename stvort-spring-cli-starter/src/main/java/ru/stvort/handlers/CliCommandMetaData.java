package ru.stvort.handlers;

import java.lang.reflect.Method;

/**
 * Contains all the data required to execute the command, such as:
 * - command name or alias binding
 * - method that handling command {@link ru.stvort.annotations.CliCommandMapping}
 * - controller object on which the method will be called {@link ru.stvort.annotations.CliController}
 *
 * @author Alexander Orudzhev
 * @see ru.stvort.annotations.CliCommandMapping
 * @see ru.stvort.annotations.CliController
 */
public class CliCommandMetaData {
    private final String command;
    private final Method commandMethod;
    private final Object beanInstance;

    public CliCommandMetaData(String command, Method commandMethod, Object beanInstance) {
        this.command = command;
        this.commandMethod = commandMethod;
        this.beanInstance = beanInstance;
    }

    public String getCommand() {
        return command;
    }

    public Method getCommandMethod() {
        return commandMethod;
    }

    public Object getBeanInstance() {
        return beanInstance;
    }
}
