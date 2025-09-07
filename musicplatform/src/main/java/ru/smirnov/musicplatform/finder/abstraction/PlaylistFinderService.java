package ru.smirnov.musicplatform.finder.abstraction;

import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionResponse;
import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;

import java.util.List;

public interface PlaylistFinderService {

    List<MusicCollectionShortcutProjection> searchPlaylists(String searchRequest, Long userId, boolean savedOnly);

    List<MusicCollectionShortcutProjection> getOwnedPlaylists(Long userId);

    List<MusicCollectionShortcutProjection> getSavedPlaylists(Long userId);

    MusicCollectionResponse getPlaylistById(Long playlistId, DataForToken tokenData);
}
