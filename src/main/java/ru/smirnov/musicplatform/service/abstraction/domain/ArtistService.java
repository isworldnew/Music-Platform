package ru.smirnov.musicplatform.service.abstraction.domain;

import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistRequest;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistResponse;
import ru.smirnov.musicplatform.dto.domain.artist.ExtendedArtistResponse;

public interface ArtistService {

    Long createArtist(ArtistRequest dto, DataForToken tokenData);

    void updateArtist(Long artistId, ArtistRequest dto, DataForToken tokenData);

//    ArtistResponse getArtistDataById(Long artistId);
//
//    ExtendedArtistResponse getExtendedArtistDataById(Long artistId);
}
