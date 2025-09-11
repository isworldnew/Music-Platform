package ru.smirnov.musicplatform.dto.audience.distributor;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class DistributorResponse {

     private Long distributorId;

     private Long accountId;

     private String username;

     private String name;

     private String description;

     private String distributorType;

     private String registrationDate;
}
