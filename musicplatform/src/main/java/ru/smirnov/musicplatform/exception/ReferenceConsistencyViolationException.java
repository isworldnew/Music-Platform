package ru.smirnov.musicplatform.exception;

public class ReferenceConsistencyViolationException extends RuntimeException {
    public ReferenceConsistencyViolationException(String message) {
        super(message);
    }
}
