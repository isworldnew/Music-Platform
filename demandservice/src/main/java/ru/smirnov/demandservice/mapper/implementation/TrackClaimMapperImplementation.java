package ru.smirnov.demandservice.mapper.implementation;

import org.springframework.stereotype.Component;
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

}
