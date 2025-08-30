package ru.smirnov.musicplatform.mapper.abstraction;

import ru.smirnov.musicplatform.dto.tmp.ArtistSocialNetworkResponse;
import ru.smirnov.musicplatform.entity.relation.ArtistsSocialNetworks;

public interface ArtistSocialNetworkMapper {

    ArtistSocialNetworkResponse artistSocialNetworkEntityToArtistSocialNetworkResponse(ArtistsSocialNetworks entity);

}
