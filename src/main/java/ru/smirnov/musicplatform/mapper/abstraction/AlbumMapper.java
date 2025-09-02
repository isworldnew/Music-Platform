package ru.smirnov.musicplatform.mapper.abstraction;

import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionRequest;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionShortcutResponse;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.finder.SearchResult;


public interface AlbumMapper {

    Album musicCollectionRequestToAlbumEntity(MusicCollectionRequest dto);
//
//    MusicCollectionShortcutResponse albumEntityToMusicCollectionShortcutResponse(Album album, boolean albumIsSaved);
//
//    MusicCollectionShortcutResponse albumEntityToMusicCollectionShortcutResponse(Album album);

    MusicCollectionShortcutResponse albumEntityToMusicCollectionShortcutResponse(Album album, SearchResult searchResultFor);


}
