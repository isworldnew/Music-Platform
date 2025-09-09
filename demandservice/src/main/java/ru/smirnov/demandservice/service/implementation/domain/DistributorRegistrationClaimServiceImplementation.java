package ru.smirnov.demandservice.service.implementation.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.demandservice.dto.DistributorRegistrationClaimRequest;
import ru.smirnov.demandservice.entity.domain.DistributorRegistrationClaim;
import ru.smirnov.demandservice.mapper.abstraction.DistributorRegistrationClaimMapper;
import ru.smirnov.demandservice.repository.DistributorRegistrationClaimRepository;
import ru.smirnov.demandservice.service.abstraction.auxiliary.ClaimAssignService;
import ru.smirnov.demandservice.service.abstraction.domain.DistributorRegistrationClaimService;

@Service
public class DistributorRegistrationClaimServiceImplementation implements DistributorRegistrationClaimService {

    private final DistributorRegistrationClaimRepository distributorRegistrationClaimRepository;
    private final DistributorRegistrationClaimMapper distributorRegistrationClaimMapper;
    private final ClaimAssignService claimAssignService;

    @Autowired
    public DistributorRegistrationClaimServiceImplementation(
            DistributorRegistrationClaimRepository distributorRegistrationClaimRepository,
            DistributorRegistrationClaimMapper distributorRegistrationClaimMapper,
            ClaimAssignService claimAssignService
    ) {
        this.distributorRegistrationClaimRepository = distributorRegistrationClaimRepository;
        this.distributorRegistrationClaimMapper = distributorRegistrationClaimMapper;
        this.claimAssignService = claimAssignService;
    }

    @Override
    public Long addDistributorRegistrationClaim(DistributorRegistrationClaimRequest dto) {
        Long adminId = this.claimAssignService.assignTo();

        DistributorRegistrationClaim claim = this.distributorRegistrationClaimMapper.distributorRegistrationClaimRequestToDistributorRegistrationClaimEntity(
                dto, adminId
        );

        return this.distributorRegistrationClaimRepository.save(claim).getId();
    }
}
