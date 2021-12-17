package ru.stvort.handlers.exceptions;

public class CliCommandInvocationException extends RuntimeException {
    public CliCommandInvocationException(String message) {
        super(message);
    }

    public CliCommandInvocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
