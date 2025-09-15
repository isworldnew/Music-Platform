package ru.smirnov.demandservice.dto;

import lombok.Data;
import ru.smirnov.dtoregistry.entity.auxiliary.DemandStatus;

@Data
public class DistributorRegistrationClaimShortcutResponse {

    private Long id;

    private String creationDateTime;

    private String expirationDateTime;

    private DemandStatus status;
}
