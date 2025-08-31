package ru.smirnov.musicplatform.dto.domain.artist;

import lombok.Data;

import java.util.List;

@Data
public class ArtistResponse {

    private Long id;

    private String name;

    private String description;

    private String coverReference;

    private List<ArtistSocialNetworkResponse> socialNetworks;
}
