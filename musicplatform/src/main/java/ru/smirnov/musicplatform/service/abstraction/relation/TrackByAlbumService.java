package ru.smirnov.musicplatform.service.abstraction.relation;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;


public interface TrackByAlbumService {

    Long addTrack(Long albumId, Long trackId, DataForToken tokenData);

    void removeTrack(Long albumId, Long trackId, DataForToken tokenData);
}
