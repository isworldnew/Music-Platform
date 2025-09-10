package ru.smirnov.demandservice.dto;

import lombok.Data;
import ru.smirnov.dtoregistry.entity.auxiliary.DemandStatus;

import java.time.OffsetDateTime;

@Data
public class DistributorRegistrationClaimResponse {

    private Long id;

    private Long adminId;

    private OffsetDateTime creationDateTime ;

    private OffsetDateTime expirationDateTime;

    private DemandStatus status;

    private String name;

    private String description;

    private String distributorType;

    private String username;

    private String password;
}
