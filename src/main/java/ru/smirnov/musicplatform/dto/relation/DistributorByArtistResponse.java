package ru.smirnov.musicplatform.dto.relation;

import lombok.Data;

@Data
public class DistributorByArtistResponse {

    private Long id;

    private Long distributorId;

    private String name;

    private String type;

    private String status;

}
