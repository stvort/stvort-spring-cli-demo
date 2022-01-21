package ru.stvort.handlers.exceptions;

public class CliDuplicateCommandException extends RuntimeException{
    public CliDuplicateCommandException(String message) {
        super(message);
    }
}
