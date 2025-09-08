package ru.smirnov.demandservice.mapper.abstraction;

import ru.smirnov.demandservice.dto.DistributorRegistrationClaimRequest;
import ru.smirnov.demandservice.entity.domain.DistributorRegistrationClaim;

public interface DistributorRegistrationClaimMapper {

    DistributorRegistrationClaim distributorRegistrationClaimRequestToDistributorRegistrationClaimEntity(DistributorRegistrationClaimRequest dto, Long adminId);
}
