package ru.smirnov.musicplatform.mapper.implementation;

import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.tmp.MusicCollectionRequest;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.mapper.abstraction.AlbumMapper;

@Component
public class AlbumMapperImplementation implements AlbumMapper {

    @Override
    public Album musicCollectionRequestToAlbumEntity(MusicCollectionRequest dto) {
        Album album = new Album();
        album.setName(dto.getName());
        album.setDescription(dto.getDescription());
        return album;
    }
}
