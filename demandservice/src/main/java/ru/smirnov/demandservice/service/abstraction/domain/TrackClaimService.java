package ru.smirnov.demandservice.service.abstraction.domain;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.dtoregistry.dto.domain.TrackAccessLevelRequest;
import ru.smirnov.dtoregistry.dto.domain.TrackClaimRequest;

public interface TrackClaimService {

    Long addTrackClaim(Long trackId, DataForToken tokenData);

    void processTrackClaim(Long claimId, TrackClaimRequest dto);
}
