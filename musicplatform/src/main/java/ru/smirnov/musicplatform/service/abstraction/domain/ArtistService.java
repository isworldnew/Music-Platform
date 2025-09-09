package ru.smirnov.musicplatform.service.abstraction.domain;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistRequest;

public interface ArtistService {

    Long createArtist(ArtistRequest dto, DataForToken tokenData);

    void updateArtist(Long artistId, ArtistRequest dto, DataForToken tokenData);

//    ArtistResponse getArtistDataById(Long artistId);
//
//    ExtendedArtistResponse getExtendedArtistDataById(Long artistId);
}
