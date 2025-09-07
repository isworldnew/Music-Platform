package ru.smirnov.musicplatform.exception;

public class UsernameOccupiedException extends RuntimeException {
    public UsernameOccupiedException(String message) { super(message); }
}
