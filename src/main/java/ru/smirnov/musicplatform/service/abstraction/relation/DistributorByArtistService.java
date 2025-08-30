package ru.smirnov.musicplatform.service.abstraction.relation;

import ru.smirnov.musicplatform.entity.auxiliary.enums.DistributionStatus;

public interface DistributorByArtistService {

    Long save(Long distributorId, Long artistId, DistributionStatus status);

}
