package ru.smirnov.musicplatform.mapper.abstraction;

import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionRequest;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionResponse;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.entity.domain.Playlist;
import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;

import java.util.List;

public interface PlaylistMapper {

    Playlist musicCollectionRequestToPlaylistEntity(MusicCollectionRequest dto, User user);

    MusicCollectionResponse playlistEntityToMusicCollectionResponse(Playlist playlist, List<TrackShortcutProjection> tracks);
}
