package ru.smirnov.musicplatform.mapper.abstraction;

import ru.smirnov.musicplatform.dto.domain.artist.ArtistSocialNetworkResponse;
import ru.smirnov.musicplatform.entity.relation.ArtistsSocialNetworks;

public interface ArtistSocialNetworkMapper {

    ArtistSocialNetworkResponse artistSocialNetworkEntityToArtistSocialNetworkResponse(ArtistsSocialNetworks entity);

}
