package ru.smirnov.demandservice.service.implementation.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.demandservice.repository.DistributorRegistrationClaimRepository;
import ru.smirnov.demandservice.service.abstraction.domain.DistributorRegistrationClaimService;

@Service
public class DistributorRegistrationClaimServiceImplementation implements DistributorRegistrationClaimService {

    private final DistributorRegistrationClaimRepository distributorRegistrationClaimRepository;

    @Autowired
    public DistributorRegistrationClaimServiceImplementation(DistributorRegistrationClaimRepository distributorRegistrationClaimRepository) {
        this.distributorRegistrationClaimRepository = distributorRegistrationClaimRepository;
    }
}
