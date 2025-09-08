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

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 8, message = "Username must be at least 8 characters long")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

}
