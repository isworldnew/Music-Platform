package ru.smirnov.dtoregistry.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.smirnov.dtoregistry.dto.authentication.LoginRequest;
import ru.smirnov.dtoregistry.entity.auxiliary.DistributorType;

@Data @AllArgsConstructor @NoArgsConstructor
public class DistributorRegistrationMessage {

    private LoginRequest accountData;

    private String name;

    private String description;

    private DistributorType distributorType;

}
