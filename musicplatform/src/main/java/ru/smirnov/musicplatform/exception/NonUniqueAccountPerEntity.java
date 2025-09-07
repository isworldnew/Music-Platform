package ru.smirnov.musicplatform.exception;

public class NonUniqueAccountPerEntity extends RuntimeException {
    public NonUniqueAccountPerEntity(String message) {
        super(message);
    }
}
