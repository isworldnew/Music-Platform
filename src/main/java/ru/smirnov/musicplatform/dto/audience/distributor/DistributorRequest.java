package ru.smirnov.musicplatform.dto.audience.distributor;

import lombok.Data;

@Data
public class DistributorRequest {

    private String name;

    private String description;

    private String distributorType;
}
