package ru.smirnov.musicplatform.precondition.abstraction.relation;

import ru.smirnov.musicplatform.entity.relation.ArtistsSocialNetworks;

import java.util.Set;

public interface ArtistSocialNetworkPreconditionService {

    ArtistsSocialNetworks getByIdIfExists(Long recordId);

    Set<ArtistsSocialNetworks> findByArtistId(Long artistId);

    void checkSocialNetworkNameUniqueness(Long artistId, String socialNetworkName);

}
