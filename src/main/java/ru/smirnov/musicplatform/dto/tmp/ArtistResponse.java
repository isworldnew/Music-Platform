package ru.smirnov.musicplatform.dto.tmp;

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
