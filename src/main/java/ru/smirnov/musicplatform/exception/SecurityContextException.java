package ru.smirnov.musicplatform.exception;

public class SecurityContextException extends RuntimeException {
    public SecurityContextException(String message) {
        super(message);
    }
}
