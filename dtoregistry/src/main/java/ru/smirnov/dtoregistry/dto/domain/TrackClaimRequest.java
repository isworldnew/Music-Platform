package ru.smirnov.dtoregistry.dto.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.smirnov.dtoregistry.validation.annotation.DemandStatus;
import ru.smirnov.dtoregistry.validation.annotation.TrackAccessLevel;

@Data @NoArgsConstructor @AllArgsConstructor
public class TrackClaimRequest {

    @NotBlank @DemandStatus
    private String demandStatus;

    @NotBlank @TrackAccessLevel
    private String accessLevel;
}
