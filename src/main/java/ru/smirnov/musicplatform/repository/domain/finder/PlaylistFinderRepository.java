package ru.smirnov.musicplatform.repository.domain.finder;

import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;

import java.util.List;

public interface PlaylistFinderRepository {

    List<MusicCollectionShortcutProjection> searchPlaylists(String searchRequest, Long userId, boolean savedOnly);

    List<MusicCollectionShortcutProjection> getOwnedPlaylists(Long userId);

    List<MusicCollectionShortcutProjection> getSavedPlaylists(Long userId);
}
