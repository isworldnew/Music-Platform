package ru.smirnov.musicplatform.dto.audience.distributor;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.smirnov.musicplatform.validation.annotation.DistributionType;

@Data
public class DistributorRequest {

    @NotBlank
    private String name;

    private String description;

    @NotBlank @DistributionType
    private String distributorType;
}
