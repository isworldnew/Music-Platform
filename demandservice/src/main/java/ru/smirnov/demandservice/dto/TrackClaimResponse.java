package ru.smirnov.demandservice.dto;

import lombok.Data;
import ru.smirnov.dtoregistry.entity.auxiliary.DemandStatus;

@Data
public class TrackClaimResponse {

    private Long id;

    private Long userId;

    private Long trackId;

    private Long adminId;

    private DemandStatus status;

    private String creationDateTime;

    private String expirationDateTime;
}
