package ru.smirnov.musicplatform.mapper.implementation;

import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionOwnerResponse;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionRequest;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionResponse;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.entity.domain.Playlist;
import ru.smirnov.musicplatform.mapper.abstraction.PlaylistMapper;
import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;

import java.util.List;

@Component
public class PlaylistMapperImplementation implements PlaylistMapper {

    @Override
    public Playlist musicCollectionRequestToPlaylistEntity(MusicCollectionRequest dto, User user) {
        Playlist playlist = new Playlist();
        playlist.setName(dto.getName());
        playlist.setDescription(dto.getDescription());
        return playlist;
    }

    @Override
    public MusicCollectionResponse playlistEntityToMusicCollectionResponse(Playlist playlist, List<TrackShortcutProjection> tracks) {
        MusicCollectionResponse dto = new MusicCollectionResponse();
        dto.setId(playlist.getId());
        dto.setName(playlist.getName());
        dto.setDescription(playlist.getDescription());
        dto.setNumberOfPlays(playlist.getNumberOfPlays());
        dto.setImageReference(playlist.getImageReference());
        dto.setUploadDateTime(playlist.getUploadDateTime().toString());
        dto.setAccessLevel(playlist.getAccessLevel().name());
        dto.setOwner(
                new MusicCollectionOwnerResponse(
                        playlist.getUser().getId(),
                        playlist.getUser().getAccount().getUsername())
        );
        dto.setIsSaved(null);
        dto.setTracks(tracks);
        return dto;
    }
}
