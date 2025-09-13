package ru.smirnov.demandservice.precondition.abstraction;

import ru.smirnov.demandservice.entity.domain.TrackClaim;
import ru.smirnov.dtoregistry.dto.domain.TrackClaimRequest;

public interface TrackClaimPreconditionService {
    TrackClaim getByIdIfExists(Long claimId);

    TrackClaim getByIdIfExistsAndBelongsToAdmin(Long claimId, Long adminId);

    TrackClaim processClaim(Long claimId, Long adminId, TrackClaimRequest dto);
}
