package ru.smirnov.musicplatform.repository.domain.finder;

import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionShortcutResponse;
import ru.smirnov.musicplatform.entity.domain.Album;

import java.util.List;

public interface AlbumFinderRepository {

    List<Album> searchAlbums(String searchRequest);

    List<Album> searchSavedAlbums(String searchRequest, Long userId);

}
