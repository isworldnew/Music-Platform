package ru.smirnov.dtoregistry.entity.auxiliary;

import lombok.Getter;

// из:
// package ru.smirnov.musicplatform.kafka.consumer.implementation;


@Getter
public enum TrackStatus {

    UPLOADED_AND_HIDDEN(false), PUBLISHED(true), BANNED(false);

    private final boolean available;

    TrackStatus(boolean available) {
        this.available = available;
    }
}
