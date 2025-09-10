package ru.smirnov.demandservice.aspect.abstraction;

import ru.smirnov.dtoregistry.dto.domain.DemandStatusRequest;

public interface DistributorClaimAspect {
    void processDistributorClaim(Long claimId, DemandStatusRequest dto);
}
