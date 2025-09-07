package ru.smirnov.musicplatform.dto.domain.musiccollection;

import lombok.Data;

@Data
public class MusicCollectionShortcutResponse {

    private Long id;

    private String name;

    private String coverReference;

    private String accessLevel;

    private Boolean isSaved;

    private MusicCollectionOwnerResponse owner;
}
