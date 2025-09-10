package ru.smirnov.musicplatform.dto.audience.distributor;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.smirnov.musicplatform.validation.annotation.DistributorType;

@Data
public class DistributorRequest {

    @NotBlank
    private String name;

    private String description;

    @NotBlank @DistributorType
    private String distributorType;
}
