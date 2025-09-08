package ru.smirnov.dtoregistry.dto.kafka;

import lombok.Data;

@Data
public class DistributorRegistrationRequest {

    private String name;

    private String description;

    private String distributorType;

    private String username;

    private String password;
}
