package ru.smirnov.demandservice.service.abstraction.domain;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;

public interface TrackClaimService {

    Long addTrackClaim(Long trackId, DataForToken tokenData);

}
