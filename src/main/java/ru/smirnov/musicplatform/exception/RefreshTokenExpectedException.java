package ru.smirnov.musicplatform.exception;

public class RefreshTokenExpectedException extends RuntimeException {
    public RefreshTokenExpectedException(String message) {
        super(message);
    }
}
