package ru.stvort.handlers.exceptions;

public class CliCommandNotFoundException extends RuntimeException {
    public CliCommandNotFoundException(String message) {
        super(message);
    }
}
