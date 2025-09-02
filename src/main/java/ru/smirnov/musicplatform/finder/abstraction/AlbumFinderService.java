package ru.smirnov.musicplatform.finder.abstraction;

import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionShortcutResponse;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.finder.SearchResult;

import java.util.List;

public interface AlbumFinderService {

    List<MusicCollectionShortcutResponse> searchAlbums(String searchRequest, Long userId, boolean savedOnly);

    // метод чтения альбома для гостя и пользователя

    // метод чтения альбома для дистрибьютора (с историей дистрибьюции)

    // метод чтения шорткатов альбомов для гостя и пользователя

    // метод чтения шорткатов альбомов для дистрибьютора

}
