package ru.smirnov.musicplatform.mapper.abstraction;

import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionRequest;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionResponse;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionShortcutResponse;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;

import java.util.List;
import java.util.Map;


public interface AlbumMapper {

    Album musicCollectionRequestToAlbumEntity(MusicCollectionRequest dto, Artist artist);
//
//    MusicCollectionShortcutResponse albumEntityToMusicCollectionShortcutResponse(Album album, boolean albumIsSaved);
//
//    MusicCollectionShortcutResponse albumEntityToMusicCollectionShortcutResponse(Album album);

    MusicCollectionShortcutResponse albumEntityToMusicCollectionShortcutResponse(Album album, Boolean isSaved);

    MusicCollectionResponse albumEntityToMusicCollectionResponse(Album album, List<TrackShortcutProjection> tracks);
}
