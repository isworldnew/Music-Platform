package ru.smirnov.dtoregistry.exception;

public class SecurityContextException extends RuntimeException {
    public SecurityContextException(String message) {
        super(message);
    }
}
