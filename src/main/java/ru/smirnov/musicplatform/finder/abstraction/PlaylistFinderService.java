package ru.smirnov.musicplatform.finder.abstraction;

import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;

import java.util.List;

public interface PlaylistFinderService {

    List<MusicCollectionShortcutProjection> searchPlaylists(String searchRequest, Long userId, boolean savedOnly);

    List<MusicCollectionShortcutProjection> getOwnedPlaylists(Long userId);

    List<MusicCollectionShortcutProjection> getSavedPlaylists(Long userId);
}
