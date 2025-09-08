package ru.smirnov.demandservice.entity.auxiliary;

import lombok.Getter;

@Getter
public enum TTL {

    TRACK_CLAIM(21 * 24 * 60 * 60 * 1000L), // 21 день
    DISTRIBUTOR_REGISTRATION_REQUEST(21 * 24 * 60 * 60 * 1000L); // 21 день

    private final Long timeToLiveInMilliseconds;

    TTL(Long timeToLiveInMilliseconds) {
        this.timeToLiveInMilliseconds = timeToLiveInMilliseconds;
    }
}
