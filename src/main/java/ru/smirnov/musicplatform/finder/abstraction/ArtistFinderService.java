package ru.smirnov.musicplatform.finder.abstraction;

import ru.smirnov.musicplatform.dto.domain.artist.ArtistResponse;

import java.util.List;

public interface ArtistFinderService {

    List<ArtistResponse> searchArtists(String searchRequest);

    List<ArtistResponse> searchArtists(String searchRequest, Long distributorId);
}
