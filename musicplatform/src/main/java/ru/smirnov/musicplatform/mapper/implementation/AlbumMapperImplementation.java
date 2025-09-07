package ru.smirnov.musicplatform.mapper.implementation;

import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionOwnerResponse;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionRequest;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionResponse;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionShortcutResponse;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.mapper.abstraction.AlbumMapper;
import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;

import java.util.List;
import java.util.Map;

@Component
public class AlbumMapperImplementation implements AlbumMapper {

    @Override
    public Album musicCollectionRequestToAlbumEntity(MusicCollectionRequest dto) {
        Album album = new Album();
        album.setName(dto.getName());
        album.setDescription(dto.getDescription());
        return album;
    }

    @Override
    public MusicCollectionShortcutResponse albumEntityToMusicCollectionShortcutResponse(Album album, Boolean isSaved) {

        MusicCollectionOwnerResponse owner = new MusicCollectionOwnerResponse(album.getArtist().getId(), album.getArtist().getName());

        MusicCollectionShortcutResponse dto = new MusicCollectionShortcutResponse();

        dto.setId(album.getId());
        dto.setOwner(owner);
        dto.setName(album.getName());
        dto.setCoverReference(album.getImageReference());
        dto.setIsSaved(isSaved);
        dto.setAccessLevel(album.getAccessLevel().name());

        if (!album.getAccessLevel().isAvailable())
            dto.setCoverReference(null);

        return dto;
    }

    @Override
    public MusicCollectionResponse albumEntityToMusicCollectionResponse(Album album, List<TrackShortcutProjection> tracks) {
        MusicCollectionResponse dto = new MusicCollectionResponse();
        dto.setId(album.getId());
        dto.setName(album.getName());
        dto.setDescription(album.getDescription());
        dto.setNumberOfPlays(album.getNumberOfPlays());
        dto.setImageReference(album.getImageReference());
        dto.setUploadDateTime(album.getUploadDateTime());
        dto.setAccessLevel(album.getAccessLevel().name());
        dto.setOwner(new MusicCollectionOwnerResponse(album.getArtist().getId(), album.getArtist().getName()));
        dto.setIsSaved(null);
        dto.setTracks(tracks);
        return dto;
    }
}
