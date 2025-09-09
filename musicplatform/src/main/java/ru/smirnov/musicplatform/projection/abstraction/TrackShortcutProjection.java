package ru.smirnov.musicplatform.projection.abstraction;

import ru.smirnov.dtoregistry.entity.auxiliary.TrackStatus;

public interface TrackShortcutProjection {

    Long getId();

    String getName();

    Long getArtistId();

    String getArtistName();

    TrackStatus getStatus();

    String getImageReference();

    Boolean getSaved();
}
