package ru.smirnov.demandservice.precondition.abstraction;

import ru.smirnov.demandservice.entity.domain.DistributorRegistrationClaim;
import ru.smirnov.dtoregistry.dto.domain.DemandStatusRequest;

public interface DistributorClaimPreconditionService {

    DistributorRegistrationClaim getByidIfExists(Long claimId);

    DistributorRegistrationClaim getByidIfExistsAndBelongsToAdmin(Long claimId, Long adminId);

    DistributorRegistrationClaim processClaim(Long claimId, Long adminId, DemandStatusRequest dto);
}
