package ru.smirnov.musicplatform.repository.domain.finder;

import ru.smirnov.musicplatform.entity.domain.Artist;

import java.util.List;

public interface ArtistFinderRepository {

    List<Artist> searchArtists(String searchRequest);

}
