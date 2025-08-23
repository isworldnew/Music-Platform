package ru.smirnov.musicplatform.exception;

public class CoverSizeExcessException extends RuntimeException {
    public CoverSizeExcessException(String message) {
        super(message);
    }
}
