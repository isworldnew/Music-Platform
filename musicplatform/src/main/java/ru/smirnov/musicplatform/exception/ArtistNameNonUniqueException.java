package ru.smirnov.musicplatform.exception;

public class ArtistNameNonUniqueException extends RuntimeException {
    public ArtistNameNonUniqueException(String message) {
        super(message);
    }
}
