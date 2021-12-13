package ru.stvort.handlers;

import java.lang.reflect.Method;

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
