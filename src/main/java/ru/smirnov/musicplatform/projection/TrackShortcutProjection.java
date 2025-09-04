package ru.smirnov.musicplatform.projection;

import ru.smirnov.musicplatform.entity.auxiliary.enums.TrackStatus;

public interface TrackShortcutProjection {

    Long getId();

    String getName();

    Long getArtistId();

    String getArtistName();

    TrackStatus getStatus();

    String getImageReference();

    Boolean getSaved();
}
