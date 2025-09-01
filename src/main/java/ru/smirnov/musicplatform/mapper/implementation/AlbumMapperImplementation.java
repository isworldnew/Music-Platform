package ru.smirnov.musicplatform.mapper.implementation;

import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionOwnerResponse;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionRequest;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionShortcutResponse;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.mapper.abstraction.AlbumMapper;

import java.util.List;

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
    public MusicCollectionShortcutResponse albumEntityToMusicCollectionShortcutResponse(Album album, boolean albumIsSaved) {

        MusicCollectionShortcutResponse dto = new MusicCollectionShortcutResponse();

        boolean albumIsAvailable = album.getAccessLevel().isAvailable();

        MusicCollectionOwnerResponse owner = new MusicCollectionOwnerResponse(album.getArtist().getId(), album.getArtist().getName());

        dto.setId(album.getId());
        dto.setName(album.getName());
        dto.setCoverReference(album.getImageReference());
        dto.setAccessLevel(album.getAccessLevel().name());
        dto.setIsSaved(albumIsSaved);
        dto.setOwner(owner);

        if (!albumIsAvailable) {
            dto.setAccessLevel(album.getAccessLevel().name());
            dto.setCoverReference(null);
        }

        return dto;
    }

    @Override
    public MusicCollectionShortcutResponse albumEntityToMusicCollectionShortcutResponse(Album album) {
        // сюда должны попадать только доступные (PUBLIC) альбомы
        if (!album.getAccessLevel().isAvailable())
            throw new IllegalStateException("albumEntityToMusicCollectionShortcutResponse(Album album) can be used only with PUBLIC albums: pre-check album's access level before this method call");

        MusicCollectionOwnerResponse owner = new MusicCollectionOwnerResponse(album.getArtist().getId(), album.getArtist().getName());

        MusicCollectionShortcutResponse dto = new MusicCollectionShortcutResponse();
        dto.setId(album.getId());
        dto.setName(album.getName());
        dto.setCoverReference(album.getImageReference());
        dto.setAccessLevel(album.getAccessLevel().name());
        dto.setIsSaved(null);
        dto.setOwner(owner);
        return dto;
    }

}
