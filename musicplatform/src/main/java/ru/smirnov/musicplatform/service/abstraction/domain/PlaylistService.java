package ru.smirnov.musicplatform.service.abstraction.domain;

import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionAccessLevelRequest;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionRequest;

public interface PlaylistService {

    Long createPlaylist(MusicCollectionRequest dto, DataForToken tokenData);

    void updatePlaylist(Long playlistId, MusicCollectionRequest dto, DataForToken tokenData);

    void updatePlaylistAccessLevel(Long playlistId, MusicCollectionAccessLevelRequest dto, DataForToken tokenData);

    void deletePlaylist(Long playlistId, DataForToken tokenData);
}
