package ru.smirnov.demandservice.precondition.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.demandservice.entity.domain.TrackClaim;
import ru.smirnov.demandservice.precondition.abstraction.TrackClaimPreconditionService;
import ru.smirnov.demandservice.repository.TrackClaimRepository;
import ru.smirnov.dtoregistry.dto.domain.TrackClaimRequest;
import ru.smirnov.dtoregistry.entity.auxiliary.DemandStatus;
import ru.smirnov.dtoregistry.exception.BadRequestException;
import ru.smirnov.dtoregistry.exception.ConflictException;
import ru.smirnov.dtoregistry.exception.ForbiddenException;
import ru.smirnov.dtoregistry.exception.NotFoundException;

import java.util.Arrays;
import java.util.List;

@Service
public class TrackClaimPreconditionServiceImplementation implements TrackClaimPreconditionService {

    private final TrackClaimRepository trackClaimRepository;

    @Autowired
    public TrackClaimPreconditionServiceImplementation(TrackClaimRepository trackClaimRepository) {
        this.trackClaimRepository = trackClaimRepository;
    }

    @Override
    public TrackClaim getByIdIfExists(Long claimId) {
        return this.trackClaimRepository.findById(claimId).orElseThrow(
                () -> new NotFoundException("Track Claim (id=" + claimId + ") was not found")
        );
    }

    @Override
    public TrackClaim getByIdIfExistsAndBelongsToAdmin(Long claimId, Long adminId) {
        TrackClaim claim = this.getByIdIfExists(claimId);

        if (!claim.getAdminId().equals(adminId))
            throw new ForbiddenException("Claim (id=" + claimId + ") doesn't assigned to admin (id=" + adminId + ")");

        return claim;
    }

    @Override
    public TrackClaim processClaim(Long claimId, Long adminId, TrackClaimRequest dto) {
        TrackClaim claim = this.getByIdIfExistsAndBelongsToAdmin(claimId, adminId);

        if (claim.getStatus().isModifying())
            throw new ConflictException("Track claim (id=" + claim.getId() + ") is COMPLETED or DENIED");

        List<String> modifyingStatuses = Arrays.stream(DemandStatus.values())
                .filter(demandStatus -> demandStatus.isModifying())
                .map(demandStatus -> demandStatus.name())
                .toList();

        if (!DemandStatus.valueOf(dto.getDemandStatus()).isModifying())
            throw new BadRequestException(
                    "Track's claim processing accepts only modifying demand status: " + modifyingStatuses
            );

        return claim;
    }

}
