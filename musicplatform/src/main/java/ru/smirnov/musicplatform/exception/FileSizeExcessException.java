package ru.smirnov.musicplatform.exception;

public class FileSizeExcessException extends RuntimeException {
    public FileSizeExcessException(String message) {
        super(message);
    }
}
