package ru.smirnov.dtoregistry.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.smirnov.dtoregistry.entity.auxiliary.DistributorType;

@Data @AllArgsConstructor @NoArgsConstructor
public class DistributorRegistrationMessage {

    private String name;

    private String description;

    private DistributorType distributorType;

    private String username;

    private String password;

}
