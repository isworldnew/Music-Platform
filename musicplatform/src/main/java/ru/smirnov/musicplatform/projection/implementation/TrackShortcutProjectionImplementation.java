package ru.smirnov.musicplatform.projection.implementation;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.smirnov.musicplatform.entity.auxiliary.enums.TrackStatus;
import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;

@Data @AllArgsConstructor
public class TrackShortcutProjectionImplementation implements TrackShortcutProjection {

    private Long id;

    private String name;

    private Long artistId;

    private String artistName;

    private TrackStatus status;

    private String imageReference;

    private Boolean saved;

}
