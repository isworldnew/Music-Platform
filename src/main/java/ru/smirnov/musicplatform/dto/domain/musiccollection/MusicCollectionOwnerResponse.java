package ru.smirnov.musicplatform.dto.domain.musiccollection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class MusicCollectionOwnerResponse {

    private Long id;

    private String name;
}
