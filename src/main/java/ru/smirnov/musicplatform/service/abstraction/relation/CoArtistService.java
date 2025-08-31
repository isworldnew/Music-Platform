package ru.smirnov.musicplatform.service.abstraction.relation;

import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.tmp.CoArtistRequest;

public interface CoArtistService {

    Long addCoArtistToTrack(Long trackId, CoArtistRequest dto, DataForToken tokenData);

    void removeCoArtistFromTrack(Long trackId, CoArtistRequest dto, DataForToken tokenData);
}
