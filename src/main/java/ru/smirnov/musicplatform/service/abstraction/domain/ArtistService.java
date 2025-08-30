package ru.smirnov.musicplatform.service.abstraction.domain;

import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.tmp.ArtistRequest;
import ru.smirnov.musicplatform.dto.tmp.ArtistResponse;
import ru.smirnov.musicplatform.dto.tmp.ExtendedArtistResponse;

public interface ArtistService {

    Long createArtist(ArtistRequest dto, DataForToken tokenData);

    void updateArtist(Long artistId, ArtistRequest dto, DataForToken tokenData);

    ArtistResponse getArtistDataById(Long artistId);

    ExtendedArtistResponse getExtendedArtistDataById(Long artistId);
}
