package ru.smirnov.demandservice.service.abstraction.domain;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.dtoregistry.dto.domain.TrackAccessLevelRequest;

public interface TrackClaimService {

    Long addTrackClaim(Long trackId, DataForToken tokenData);

    //    @Transactional
    void processTrackClaim(Long claimId, TrackAccessLevelRequest dto);
}
