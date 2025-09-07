package ru.smirnov.demandservice.entities.auxiliary;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable @Data
public class DistributorData {

    @Column(columnDefinition = "TEXT", nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String distributorType; // валидацию вот этого типа (и сам этот тип) хорошо бы в общее место вынести...

    @Column(columnDefinition = "TEXT", nullable = false)
    private String username;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String password;
}
