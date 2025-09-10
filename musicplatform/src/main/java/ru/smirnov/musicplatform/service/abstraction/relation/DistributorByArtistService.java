package ru.smirnov.musicplatform.service.abstraction.relation;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.relation.DistributorByArtistRelationRequest;
import ru.smirnov.musicplatform.entity.auxiliary.enums.DistributionStatus;

public interface DistributorByArtistService {

    Long save(Long distributorId, Long artistId, DistributionStatus status);

    void updateRelationBetweenDistributorAndArtist(Long artistId, DistributorByArtistRelationRequest dto, DataForToken tokenData);
}
