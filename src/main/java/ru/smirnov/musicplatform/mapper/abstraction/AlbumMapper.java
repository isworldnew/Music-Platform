package ru.smirnov.musicplatform.mapper.abstraction;

import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionRequest;
import ru.smirnov.musicplatform.entity.domain.Album;

public interface AlbumMapper {

    Album musicCollectionRequestToAlbumEntity(MusicCollectionRequest dto);

}
