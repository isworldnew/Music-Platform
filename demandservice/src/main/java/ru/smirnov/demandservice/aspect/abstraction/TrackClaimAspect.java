package ru.smirnov.demandservice.aspect.abstraction;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.dtoregistry.dto.domain.TrackAccessLevelRequest;

public interface TrackClaimAspect {

    void checkClaimExistence(Long claimId, TrackAccessLevelRequest dto, DataForToken tokenData);
}
