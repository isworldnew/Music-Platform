package ru.smirnov.demandservice.dto;

import lombok.Data;
import ru.smirnov.dtoregistry.entity.auxiliary.DemandStatus;

import java.time.OffsetDateTime;

@Data
public class DistributorRegistrationClaimShortcutResponse {

    private Long id;

    private OffsetDateTime creationDateTime;

    private OffsetDateTime expirationDateTime;

    private DemandStatus status;
}
