package ru.smirnov.demandservice.mapper.implementation;

import org.springframework.stereotype.Component;
import ru.smirnov.demandservice.entity.auxiliary.TTL;
import ru.smirnov.demandservice.entity.domain.TrackClaim;
import ru.smirnov.demandservice.mapper.abstraction.TrackClaimMapper;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

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
