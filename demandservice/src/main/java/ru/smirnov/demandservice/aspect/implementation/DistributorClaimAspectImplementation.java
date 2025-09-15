package ru.smirnov.demandservice.aspect.implementation;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smirnov.demandservice.aspect.abstraction.DistributorClaimAspect;
import ru.smirnov.demandservice.entity.domain.DistributorRegistrationClaim;
import ru.smirnov.demandservice.repository.DistributorRegistrationClaimRepository;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.dtoregistry.dto.domain.DemandStatusRequest;
import ru.smirnov.dtoregistry.entity.auxiliary.DemandStatus;
import ru.smirnov.dtoregistry.exception.BadRequestException;
import ru.smirnov.dtoregistry.exception.ConflictException;
import ru.smirnov.dtoregistry.exception.ForbiddenException;
import ru.smirnov.dtoregistry.exception.NotFoundException;

import java.util.Arrays;
import java.util.List;

// @Aspect
// @Component
public class DistributorClaimAspectImplementation implements DistributorClaimAspect {

    private final DistributorRegistrationClaimRepository distributorRegistrationClaimRepository;

    @Autowired
    public DistributorClaimAspectImplementation(DistributorRegistrationClaimRepository distributorRegistrationClaimRepository) {
        this.distributorRegistrationClaimRepository = distributorRegistrationClaimRepository;
    }

    @Override
    @Before("execution(* ru.smirnov.demandservice.service.implementation.domain.DistributorRegistrationClaimServiceImplementation.processDistributorClaim(..)) && args(claimId, dto, tokenData)")
    // а тут указывать именно путь до имплементации или достаточно до интерфейса?
    public void processDistributorClaim(Long claimId, DemandStatusRequest dto, DataForToken tokenData) {

        DistributorRegistrationClaim claim = this.distributorRegistrationClaimRepository.findById(claimId).orElse(null);

        if (claim == null)
            throw new NotFoundException("Distributor Registration Claim with id=" + claimId + " was not found");

        if (!claim.getAdminId().equals(tokenData.getEntityId()))
            throw new ForbiddenException("Claim (id=" + claimId + ") doesn't assigned to admin (id=" + tokenData.getEntityId() + ")");

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
    }

    @Override
    @Before("execution (* ru.smirnov.demandservice.service.implementation.domain.DistributorRegistrationClaimServiceImplementation.getDistributorRegistrationClaimById(..)) && args(claimId, tokenData)")
    public void getDistributorRegistrationClaimById(Long claimId, DataForToken tokenData) {
        DistributorRegistrationClaim claim = this.distributorRegistrationClaimRepository.findById(claimId).orElse(null);

        if (claim == null)
            throw new NotFoundException("Distributor Registration Claim with id=" + claimId + " was not found");

        if (!claim.getAdminId().equals(tokenData.getEntityId()))
            throw new ForbiddenException("Claim (id=" + claimId + ") doesn't assigned to admin (id=" + tokenData.getEntityId() + ")");

    }

}
