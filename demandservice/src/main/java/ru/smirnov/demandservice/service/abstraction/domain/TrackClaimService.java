package ru.smirnov.demandservice.service.abstraction.domain;

import ru.smirnov.demandservice.dto.TrackClaimResponse;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.dtoregistry.dto.domain.TrackClaimRequest;

import java.util.List;

public interface TrackClaimService {

    Long addTrackClaim(Long trackId, DataForToken tokenData);

    void processTrackClaim(Long claimId, TrackClaimRequest dto, DataForToken tokenData);

    List<TrackClaimResponse> getTrackClaims(Boolean relevantOnly, DataForToken tokenData);

    TrackClaimResponse getTrackClaimById(Long claimId, DataForToken tokenData);
}
