package ru.smirnov.musicplatform.repository.domain.finder;

import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionShortcutResponse;

import java.util.List;

public interface AlbumFinderRepository {

    List<MusicCollectionShortcutResponse> searchAlbums(String searchRequest);

    List<MusicCollectionShortcutResponse> searchSavedAlbums(String searchRequest, Long userId);

}
