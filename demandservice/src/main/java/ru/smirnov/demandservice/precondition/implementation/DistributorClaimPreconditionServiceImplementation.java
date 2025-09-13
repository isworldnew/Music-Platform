package ru.smirnov.demandservice.precondition.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.demandservice.entity.domain.DistributorRegistrationClaim;
import ru.smirnov.demandservice.precondition.abstraction.DistributorClaimPreconditionService;
import ru.smirnov.demandservice.repository.DistributorRegistrationClaimRepository;
import ru.smirnov.dtoregistry.dto.domain.DemandStatusRequest;
import ru.smirnov.dtoregistry.entity.auxiliary.DemandStatus;
import ru.smirnov.dtoregistry.exception.BadRequestException;
import ru.smirnov.dtoregistry.exception.ConflictException;
import ru.smirnov.dtoregistry.exception.ForbiddenException;
import ru.smirnov.dtoregistry.exception.NotFoundException;

import java.util.Arrays;
import java.util.List;

@Service
public class DistributorClaimPreconditionServiceImplementation implements DistributorClaimPreconditionService {

    private final DistributorRegistrationClaimRepository distributorRegistrationClaimRepository;

    @Autowired
    public DistributorClaimPreconditionServiceImplementation(DistributorRegistrationClaimRepository distributorRegistrationClaimRepository) {
        this.distributorRegistrationClaimRepository = distributorRegistrationClaimRepository;
    }

    @Override
    public DistributorRegistrationClaim getByidIfExists(Long claimId) {
        return this.distributorRegistrationClaimRepository.findById(claimId).orElseThrow(
                () -> new NotFoundException("Distributor Registration Claim with id=" + claimId + " was not found")
        );
    }

    @Override
    public DistributorRegistrationClaim getByidIfExistsAndBelongsToAdmin(Long claimId, Long adminId) {
        DistributorRegistrationClaim claim = this.getByidIfExists(claimId);

        if (!claim.getAdminId().equals(adminId))
            throw new ForbiddenException("Claim (id=" + claimId + ") doesn't assigned to admin (id=" + adminId + ")");

        return claim;
    }

    @Override
    public DistributorRegistrationClaim processClaim(Long claimId, Long adminId, DemandStatusRequest dto) {
        DistributorRegistrationClaim claim = this.getByidIfExistsAndBelongsToAdmin(claimId, adminId);

        if (claim.getStatus().equals(DemandStatus.COMPLETED))
            throw new ConflictException("Claim (id=" + claimId + ") is COMPLETED");

        List<String> modifyingStatuses = Arrays.stream(DemandStatus.values())
                .filter(demandStatus -> demandStatus.isModifying())
                .map(demandStatus -> demandStatus.name())
                .toList();

        if (!DemandStatus.valueOf(dto.getDemandStatus()).isModifying())
            throw new BadRequestException(
                    "Distributor's claim processing accepts only modifying demand status: " + modifyingStatuses
            );

        return claim;
    }

}
