package ru.smirnov.musicplatform.finder.abstraction;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistExtendedResponse;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistResponse;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistShortcutResponse;

import java.util.List;

public interface ArtistFinderService {

    List<ArtistShortcutResponse> searchArtists(String searchRequest);

    List<ArtistShortcutResponse> searchArtists(String searchRequest, Long distributorId);

    List<ArtistShortcutResponse> getDistributedArtists(Long distributorId, boolean activelyDistributed);

    ArtistResponse getArtistData(Long artistId, DataForToken tokenData);

    ArtistExtendedResponse getArtistExtendedData(Long artistId, DataForToken tokenData);
}
