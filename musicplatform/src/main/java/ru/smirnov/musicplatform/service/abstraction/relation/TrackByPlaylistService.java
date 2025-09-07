package ru.smirnov.musicplatform.service.abstraction.relation;

import ru.smirnov.musicplatform.authentication.DataForToken;

public interface TrackByPlaylistService {

    Long addTrack(Long playlistId, Long trackId, DataForToken tokenData);

    void removeTrack(Long playlistId, Long trackId, DataForToken tokenData);
}
