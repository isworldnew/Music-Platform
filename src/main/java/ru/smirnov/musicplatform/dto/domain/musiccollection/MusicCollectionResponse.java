package ru.smirnov.musicplatform.dto.domain.musiccollection;

import lombok.Data;
import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class MusicCollectionResponse {

    private Long id;

    private String name;

    private String description;

    private Long numberOfPlays;

    private String imageReference;

    private OffsetDateTime uploadDateTime;

    private String accessLevel;

    private MusicCollectionOwnerResponse owner;

    private Boolean isSaved;

    private List<TrackShortcutProjection> tracks;
}
