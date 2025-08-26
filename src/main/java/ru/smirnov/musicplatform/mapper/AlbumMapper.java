package ru.smirnov.musicplatform.mapper;

import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.domain.album.MusicCollectionToCreateDto;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.entity.domain.Artist;

import java.time.OffsetDateTime;

@Component
public class AlbumMapper {

    public Album musicCollectionToCreateDtoToAlbumEntity(MusicCollectionToCreateDto dto, Artist artist) {
        Album album = new Album();
        album.setName(dto.getName());
        album.setDescription(dto.getDescription());
        album.setArtist(artist);
        album.setUploadDateTime(OffsetDateTime.now());
        // количество прослушиваний и уровень доступа по идее должны получить в БД значение по умолчанию
        // ссылку на обложку возможно будет установить только после сохранения записи и получения её id
        return album;
    }

}
