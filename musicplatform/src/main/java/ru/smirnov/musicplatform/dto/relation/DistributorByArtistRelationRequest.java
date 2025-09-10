package ru.smirnov.musicplatform.dto.relation;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.smirnov.musicplatform.validation.annotation.DistributionStatus;

@Data
public class DistributorByArtistRelationRequest {

    private Long distributorId;

    @NotBlank @DistributionStatus
    private String distributionStatus;
}
