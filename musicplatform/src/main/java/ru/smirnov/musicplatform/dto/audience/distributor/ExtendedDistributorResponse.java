package ru.smirnov.musicplatform.dto.audience.distributor;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class ExtendedDistributorResponse {

    private Long id;

    private String name;

    private String description;

    private String distributorType;

    private String registrationDate;

    private List<DistributedArtistShortcutResponse> distributedArtists; // id исполнителя, имя и статус дистрибьюции
}
