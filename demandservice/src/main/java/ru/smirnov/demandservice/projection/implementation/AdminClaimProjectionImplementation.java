package ru.smirnov.demandservice.projection.implementation;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.smirnov.demandservice.projection.abstraction.AdminClaimProjection;

@Data @AllArgsConstructor
public class AdminClaimProjectionImplementation implements AdminClaimProjection {

    private Long id;

    private Long amountOfClaims;
}
