package ru.smirnov.musicplatform.finder.abstraction;

import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;

import java.util.List;

public interface AlbumFinderService {

    List<MusicCollectionShortcutProjection> searchAlbums(String searchRequest, Long userId, boolean savedOnly);

    List<MusicCollectionShortcutProjection> getSavedAlbums(Long userId);

    // метод чтения альбома для гостя и пользователя

    // метод чтения альбома для дистрибьютора (с историей дистрибьюции)

    // метод чтения шорткатов альбомов для гостя и пользователя

    // метод чтения шорткатов альбомов для дистрибьютора

}
