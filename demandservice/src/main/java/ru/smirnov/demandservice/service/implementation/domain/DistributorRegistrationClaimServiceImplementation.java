package ru.smirnov.demandservice.service.implementation.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.demandservice.client.AdminClient;
import ru.smirnov.demandservice.dto.DistributorRegistrationClaimRequest;
import ru.smirnov.demandservice.entity.domain.DistributorRegistrationClaim;
import ru.smirnov.demandservice.mapper.abstraction.DistributorRegistrationClaimMapper;
import ru.smirnov.demandservice.repository.DistributorRegistrationClaimRepository;
import ru.smirnov.demandservice.service.abstraction.domain.DistributorRegistrationClaimService;
import ru.smirnov.demandservice.util.Randomizer;

import java.util.List;

@Service
public class DistributorRegistrationClaimServiceImplementation implements DistributorRegistrationClaimService {

    private final DistributorRegistrationClaimRepository distributorRegistrationClaimRepository;
    private final DistributorRegistrationClaimMapper distributorRegistrationClaimMapper;
    private final AdminClient adminClient;

    @Autowired
    public DistributorRegistrationClaimServiceImplementation(
            DistributorRegistrationClaimRepository distributorRegistrationClaimRepository,
            DistributorRegistrationClaimMapper distributorRegistrationClaimMapper,
            AdminClient adminClient
    ) {
        this.distributorRegistrationClaimRepository = distributorRegistrationClaimRepository;
        this.distributorRegistrationClaimMapper = distributorRegistrationClaimMapper;
        this.adminClient = adminClient;
    }

    @Override
    public Long addDistributorRegistrationClaim(DistributorRegistrationClaimRequest dto) {
        List<Long> admins = this.adminClient.getAllEnabledAdmins();

        Long adminId = admins.get(Randomizer.getRandomIndex(admins));

        DistributorRegistrationClaim claim = this.distributorRegistrationClaimMapper.distributorRegistrationClaimRequestToDistributorRegistrationClaimEntity(
                dto, adminId
        );

        return this.distributorRegistrationClaimRepository.save(claim).getId();
    }
}
