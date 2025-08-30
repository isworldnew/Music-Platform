package ru.smirnov.musicplatform.dto.tmp;

import lombok.Data;

import java.util.List;

@Data
public class ExtendedArtistResponse extends ArtistResponse {

    private List<DistributorByArtistResponse> distributors;

}
