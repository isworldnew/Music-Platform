package ru.smirnov.demandservice.aspect.implementation;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smirnov.demandservice.aspect.abstraction.TrackClaimAspect;
import ru.smirnov.demandservice.entity.domain.TrackClaim;
import ru.smirnov.demandservice.repository.TrackClaimRepository;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.dtoregistry.dto.domain.TrackClaimRequest;
import ru.smirnov.dtoregistry.entity.auxiliary.DemandStatus;
import ru.smirnov.dtoregistry.exception.BadRequestException;
import ru.smirnov.dtoregistry.exception.ConflictException;
import ru.smirnov.dtoregistry.exception.ForbiddenException;
import ru.smirnov.dtoregistry.exception.NotFoundException;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class TrackClaimAspectImplementation implements TrackClaimAspect {

    private final TrackClaimRepository trackClaimRepository;

    @Autowired
    public TrackClaimAspectImplementation(TrackClaimRepository trackClaimRepository) {
        this.trackClaimRepository = trackClaimRepository;
    }

    @Override
    @Before("execution (* ru.smirnov.demandservice.service.implementation.domain.TrackClaimServiceImplementation.processTrackClaim(..)) && args(claimId, dto, tokenData)")
    // вот это можно было бы в pointcut вынести
    public void checkClaimExistence(Long claimId, TrackClaimRequest dto, DataForToken tokenData) {

        TrackClaim claim = this.trackClaimRepository.findById(claimId).orElse(null);
        if (claim == null)
            throw new NotFoundException("Track Claim (id=" + claimId + ") was not found");

        if (!claim.getAdminId().equals(tokenData.getEntityId()))
            throw new ForbiddenException("Claim (id=" + claimId + ") doesn't assigned to admin (id=" + tokenData.getEntityId() + ")");

        if (claim.getStatus().equals(DemandStatus.COMPLETED))
            throw new ConflictException("Track claim (id=" + claim.getId() + ") is COMPLETED");

        List<String> modifyingStatuses = Arrays.stream(DemandStatus.values())
                .filter(demandStatus -> demandStatus.isModifying())
                .map(demandStatus -> demandStatus.name())
                .toList();

        if (!DemandStatus.valueOf(dto.getDemandStatus()).isModifying())
            throw new BadRequestException(
                    "Track's claim processing accepts only modifying demand status: " + modifyingStatuses
            );

    }

    @Override
    @Before("execution (* ru.smirnov.demandservice.service.implementation.domain.TrackClaimServiceImplementation.getTrackClaimById(..)) && args(claimId, tokenData)")
    public void getTrackClaimById(Long claimId, DataForToken tokenData) {
        TrackClaim claim = this.trackClaimRepository.findById(claimId).orElse(null);
        if (claim == null)
            throw new NotFoundException("Track Claim (id=" + claimId + ") was not found");

        if (!claim.getAdminId().equals(tokenData.getEntityId()))
            throw new ForbiddenException("Claim (id=" + claimId + ") doesn't assigned to admin (id=" + tokenData.getEntityId() + ")");
    }
}
