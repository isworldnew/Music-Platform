package ru.smirnov.demandservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.smirnov.dtoregistry.validation.annotation.DistributionType;

@Data
public class DistributorRegistrationClaimRequest {

    @NotBlank
    private String name;

    private String description;

    @DistributionType
    private String distributorType;

    @NotBlank
    @Size(min = 4, message = "Username's size should be >= 4")
    private String username;

    @NotBlank
    @Size(min = 10, message = "Password's size should be >= 10")
    private String password;

}
