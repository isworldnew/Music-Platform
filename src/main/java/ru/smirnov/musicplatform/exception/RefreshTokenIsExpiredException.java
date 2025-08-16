package ru.smirnov.musicplatform.exception;

public class RefreshTokenIsExpiredException extends RuntimeException {
    public RefreshTokenIsExpiredException(String message) {
        super(message);
    }
}
