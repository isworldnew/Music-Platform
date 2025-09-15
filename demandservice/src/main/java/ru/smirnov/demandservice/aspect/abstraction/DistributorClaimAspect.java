package ru.smirnov.demandservice.aspect.abstraction;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.dtoregistry.dto.domain.DemandStatusRequest;

public interface DistributorClaimAspect {

    void processDistributorClaim(Long claimId, DemandStatusRequest dto, DataForToken tokenData);

    void getDistributorRegistrationClaimById(Long claimId, DataForToken tokenData);
}
