package ru.smirnov.musicplatform.service.abstraction.domain;

import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.tmp.MusicCollectionAccessLevelRequest;
import ru.smirnov.musicplatform.dto.tmp.MusicCollectionRequest;

public interface AlbumService {

    Long createAlbum(Long artistId, MusicCollectionRequest dto, DataForToken tokenData);

    void updateAlbum(Long artistId, Long albumId, MusicCollectionRequest dto, DataForToken tokenData);

    void updateAlbumAccessLevel(Long artistId, Long albumId, MusicCollectionAccessLevelRequest dto, DataForToken tokenData);
}
