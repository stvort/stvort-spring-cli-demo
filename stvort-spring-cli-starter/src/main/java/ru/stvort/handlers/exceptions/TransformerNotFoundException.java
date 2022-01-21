package ru.stvort.handlers.exceptions;

public class TransformerNotFoundException extends RuntimeException {
    public TransformerNotFoundException(String message) {
        super(message);
    }
}
