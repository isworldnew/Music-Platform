package ru.smirnov.demandservice.mapper.abstraction;

import ru.smirnov.demandservice.dto.DistributorRegistrationClaimRequest;
import ru.smirnov.demandservice.dto.DistributorRegistrationClaimResponse;
import ru.smirnov.demandservice.dto.DistributorRegistrationClaimShortcutResponse;
import ru.smirnov.demandservice.entity.domain.DistributorRegistrationClaim;
import ru.smirnov.dtoregistry.message.DistributorRegistrationMessage;

public interface DistributorRegistrationClaimMapper {

    DistributorRegistrationClaim distributorRegistrationClaimRequestToDistributorRegistrationClaimEntity(DistributorRegistrationClaimRequest dto, Long adminId);

    DistributorRegistrationMessage distributorRegistrationClaimToDistributorRegistrationMessage(DistributorRegistrationClaim claim);

    DistributorRegistrationClaimShortcutResponse toShortcut(DistributorRegistrationClaim claim);

    DistributorRegistrationClaimResponse toResponse(DistributorRegistrationClaim claim);
}
