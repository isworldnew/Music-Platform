package ru.smirnov.musicplatform.service.abstraction.relation;

import ru.smirnov.musicplatform.authentication.DataForToken;


public interface TrackByAlbumService {

    Long addTrack(Long albumId, Long trackId, DataForToken tokenData);

    void removeTrack(Long albumId, Long trackId, DataForToken tokenData);
}
