package ru.smirnov.demandservice.aspect.abstraction;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.dtoregistry.dto.domain.TrackClaimRequest;

public interface TrackClaimAspect {

    void checkClaimExistence(Long claimId, TrackClaimRequest dto, DataForToken tokenData);

    void getTrackClaimById(Long trackId, DataForToken tokenData);
}
