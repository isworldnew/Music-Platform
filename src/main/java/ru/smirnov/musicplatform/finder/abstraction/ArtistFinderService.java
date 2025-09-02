package ru.smirnov.musicplatform.finder.abstraction;

import ru.smirnov.musicplatform.dto.domain.artist.ArtistResponse;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistShortcutResponse;

import java.util.List;

public interface ArtistFinderService {

    List<ArtistResponse> searchArtists(String searchRequest);

}
