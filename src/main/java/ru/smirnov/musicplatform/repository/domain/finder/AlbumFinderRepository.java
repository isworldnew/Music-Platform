package ru.smirnov.musicplatform.repository.domain.finder;

import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;

import java.util.List;

public interface AlbumFinderRepository {

    List<MusicCollectionShortcutProjection> searchAlbums(String searchRequest, Long userId, boolean savedOnly);

    List<MusicCollectionShortcutProjection> getSavedAlbums(Long userId);
}
