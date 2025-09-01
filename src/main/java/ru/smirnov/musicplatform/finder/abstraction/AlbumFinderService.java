package ru.smirnov.musicplatform.finder.abstraction;

import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionShortcutResponse;

import java.util.List;

public interface AlbumFinderService {

    List<MusicCollectionShortcutResponse> globalAlbumSearch(String searchRequest);

    List<MusicCollectionShortcutResponse> globalAlbumSearch(String searchRequest, DataForToken tokenData);

    List<MusicCollectionShortcutResponse> savedAlbumSearch(String searchRequest, DataForToken tokenData);

    // метод чтения альбома для гостя и пользователя

    // метод чтения альбома для дистрибьютора (с историей дистрибьюции)

    // метод чтения шорткатов альбомов для гостя и пользователя

    // метод чтения шорткатов альбомов для дистрибьютора

}
