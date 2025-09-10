package ru.smirnov.demandservice.service.abstraction.domain;

import ru.smirnov.demandservice.dto.DistributorRegistrationClaimRequest;
import ru.smirnov.dtoregistry.dto.domain.DemandStatusRequest;

public interface DistributorRegistrationClaimService {
    Long addDistributorRegistrationClaim(DistributorRegistrationClaimRequest dto);

    void processDistributorClaim(Long claimId, DemandStatusRequest dto);
}
