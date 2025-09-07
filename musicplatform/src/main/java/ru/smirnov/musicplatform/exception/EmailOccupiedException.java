package ru.smirnov.musicplatform.exception;

public class EmailOccupiedException extends RuntimeException {
    public EmailOccupiedException(String message) {
        super(message);
    }
}
