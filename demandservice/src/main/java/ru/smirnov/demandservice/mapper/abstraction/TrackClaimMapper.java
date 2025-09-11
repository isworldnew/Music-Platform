package ru.smirnov.demandservice.mapper.abstraction;

import ru.smirnov.demandservice.dto.TrackClaimResponse;
import ru.smirnov.demandservice.entity.domain.TrackClaim;

public interface TrackClaimMapper {

    TrackClaim initTrackClaim(Long trackId, Long adminId, Long userId);

    TrackClaimResponse toResponse(TrackClaim claim);
}
