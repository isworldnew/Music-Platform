package ru.smirnov.musicplatform.service.abstraction.relation;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.relation.CoArtistRequest;

public interface CoArtistService {

    Long addCoArtistToTrack(Long trackId, CoArtistRequest dto, DataForToken tokenData);

    void removeCoArtistFromTrack(Long trackId, CoArtistRequest dto, DataForToken tokenData);
}
