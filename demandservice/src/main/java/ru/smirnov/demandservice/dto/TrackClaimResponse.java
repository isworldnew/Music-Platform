package ru.smirnov.demandservice.dto;

import lombok.Data;
import ru.smirnov.dtoregistry.entity.auxiliary.DemandStatus;

import java.time.OffsetDateTime;

@Data
public class TrackClaimResponse {

    private Long id;

    private Long userId;

    private Long trackId;

    private Long adminId;

    private DemandStatus status;

    private OffsetDateTime creationDateTime;

    private OffsetDateTime expirationDateTime;
}
