package ru.smirnov.demandservice.service.abstraction.domain;

import ru.smirnov.demandservice.dto.DistributorRegistrationClaimRequest;

public interface DistributorRegistrationClaimService {
    Long addDistributorRegistrationClaim(DistributorRegistrationClaimRequest dto);
}
