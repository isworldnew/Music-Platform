package ru.smirnov.musicplatform.service.abstraction.relation;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;

public interface SavedPlaylistService {

    Long addPlaylist(Long playlistId, DataForToken tokenData);

    void removePlaylist(Long playlistId, DataForToken tokenData);
}
