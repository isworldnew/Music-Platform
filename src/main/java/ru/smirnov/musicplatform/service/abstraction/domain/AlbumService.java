package ru.smirnov.musicplatform.service.abstraction.domain;

import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionAccessLevelRequest;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionRequest;

public interface AlbumService {

    Long createAlbum(Long artistId, MusicCollectionRequest dto, DataForToken tokenData);

    void updateAlbum(Long albumId, MusicCollectionRequest dto, DataForToken tokenData);

    void updateAlbumAccessLevel(Long albumId, MusicCollectionAccessLevelRequest dto, DataForToken tokenData);

    void deleteAlbum(Long albumId, DataForToken tokenData);
}
