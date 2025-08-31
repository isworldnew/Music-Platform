package ru.smirnov.musicplatform.mapper.abstraction;

import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionRequest;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.entity.domain.Playlist;

public interface PlaylistMapper {

    Playlist musicCollectionRequestToPlaylistEntity(MusicCollectionRequest dto, User user);

}
