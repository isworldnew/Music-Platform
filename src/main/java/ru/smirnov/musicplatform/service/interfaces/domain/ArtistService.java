package ru.smirnov.musicplatform.service.interfaces.domain;

import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.tmp.ArtistRequest;

public interface ArtistService {

    Long createArtist(ArtistRequest dto, DataForToken tokenData);

    ? updateArtist(Long artistId, DataForToken tokenData);

}
