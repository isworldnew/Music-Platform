package ru.smirnov.demandservice.mapper.implementation;

import org.springframework.stereotype.Component;
import ru.smirnov.demandservice.dto.TrackClaimResponse;
import ru.smirnov.demandservice.entity.domain.TrackClaim;
import ru.smirnov.demandservice.mapper.abstraction.TrackClaimMapper;

@Component
public class TrackClaimMapperImplementation implements TrackClaimMapper {

    @Override
    public TrackClaim initTrackClaim(Long trackId, Long adminId, Long userId) {
        TrackClaim claim = new TrackClaim();
        claim.setTrackId(trackId);
        claim.setAdminId(adminId);
        claim.setUserId(userId);
        return claim;
    }

    @Override
    public TrackClaimResponse toResponse(TrackClaim claim) {
        TrackClaimResponse dto = new TrackClaimResponse();
        dto.setId(claim.getId());
        dto.setUserId(claim.getUserId());
        dto.setTrackId(claim.getTrackId());
        dto.setAdminId(claim.getAdminId());
        dto.setStatus(claim.getStatus());
        dto.setCreationDateTime(claim.getCreationDateTime().toString());
        dto.setExpirationDateTime(claim.getExpirationDateTime().toString());
        return dto;
    }
}
