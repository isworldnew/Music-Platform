package ru.smirnov.demandservice.service.abstraction.domain;

import ru.smirnov.demandservice.dto.DistributorRegistrationClaimRequest;
import ru.smirnov.demandservice.dto.DistributorRegistrationClaimResponse;
import ru.smirnov.demandservice.dto.DistributorRegistrationClaimShortcutResponse;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.dtoregistry.dto.domain.DemandStatusRequest;

import java.util.List;

public interface DistributorRegistrationClaimService {
    Long addDistributorRegistrationClaim(DistributorRegistrationClaimRequest dto);

    void processDistributorClaim(Long claimId, DemandStatusRequest dto, DataForToken tokenData);

    List<DistributorRegistrationClaimShortcutResponse> getDistributorRegistrationClaims(Boolean relevantOnly, DataForToken tokenData);

    DistributorRegistrationClaimResponse getDistributorRegistrationClaimById(Long claimId, DataForToken tokenData);
}
