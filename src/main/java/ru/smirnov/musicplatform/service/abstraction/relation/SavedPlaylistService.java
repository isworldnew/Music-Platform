package ru.smirnov.musicplatform.service.abstraction.relation;

import ru.smirnov.musicplatform.authentication.DataForToken;

public interface SavedPlaylistService {

    Long addPlaylist(Long playlistId, DataForToken tokenData);

    void removePlaylist(Long playlistId, DataForToken tokenData);
}
