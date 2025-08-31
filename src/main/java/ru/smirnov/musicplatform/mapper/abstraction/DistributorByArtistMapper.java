package ru.smirnov.musicplatform.mapper.abstraction;

import ru.smirnov.musicplatform.dto.relation.DistributorByArtistResponse;
import ru.smirnov.musicplatform.entity.audience.Distributor;
import ru.smirnov.musicplatform.entity.auxiliary.enums.DistributionStatus;

public interface DistributorByArtistMapper {

    DistributorByArtistResponse distributorEntityToDistributorByArtistResponse(Long relationId, DistributionStatus status, Distributor distributor);

}
