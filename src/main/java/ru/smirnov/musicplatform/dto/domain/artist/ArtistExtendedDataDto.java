package ru.smirnov.musicplatform.dto.domain.artist;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.smirnov.musicplatform.dto.relation.ArtistSocialNetworkDto;
import ru.smirnov.musicplatform.projection.DistributorByArtistProjection;

import java.util.List;

@Data @NoArgsConstructor
public class ArtistExtendedDataDto {

    private Long id;

    private String name;

    private String description;

    private String coverReference;

    private List<ArtistSocialNetworkDto> socialNetworks;

    private List<DistributorByArtistProjection> distributors;

}
