package ru.smirnov.musicplatform.mapper.implementation;

import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionRequest;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.entity.domain.Playlist;
import ru.smirnov.musicplatform.mapper.abstraction.PlaylistMapper;

@Component
public class PlaylistMapperImplementation implements PlaylistMapper {

    @Override
    public Playlist musicCollectionRequestToPlaylistEntity(MusicCollectionRequest dto, User user) {
        Playlist playlist = new Playlist();
        playlist.setName(dto.getName());
        playlist.setDescription(dto.getDescription());
        return playlist;
    }
}
