package ru.smirnov.musicplatform.projection.implementation;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.smirnov.musicplatform.entity.auxiliary.enums.MusicCollectionAccessLevel;
import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;

@Data @AllArgsConstructor
public class MusicCollectionShortcutProjectionImplementation implements MusicCollectionShortcutProjection {

    private Long id;

    private String name;

    private String imageReference;

    private Long creatorId;

    private String creatorUsername;

    private MusicCollectionAccessLevel accessLevel;

    private Boolean saved;
}
