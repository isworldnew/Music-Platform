package ru.smirnov.musicplatform.dto.deprecated.domain.artist;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data @NoArgsConstructor
public class ArtistDataDto {

    private Long id;

    private String name;

    private String description;

    private String coverReference;

    private Map<String, String> socialNetworks;


}
