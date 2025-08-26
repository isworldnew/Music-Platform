package ru.smirnov.musicplatform.entity.auxiliary.enums;

public enum MusicCollectionAccessLevel {

    PRIVATE(false), PUBLIC(true);

    private final boolean available;

    MusicCollectionAccessLevel(boolean available) {
        this.available = available;
    }
}
