package ru.smirnov.musicplatform.mapper;

import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.domain.MusicCollectionAuthorDto;
import ru.smirnov.musicplatform.dto.domain.album.MusicCollectionDataDto;
import ru.smirnov.musicplatform.dto.domain.album.MusicCollectionToCreateDto;
import ru.smirnov.musicplatform.dto.domain.track.TrackShortcutDto;
import ru.smirnov.musicplatform.entity.auxiliary.enums.MusicCollectionAccessLevel;
import ru.smirnov.musicplatform.entity.auxiliary.hierarchy.MusicCollection;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.entity.domain.Artist;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class MusicCollectionMapper {

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

    public MusicCollectionDataDto musicCollectionToMusicCollectionDataDto(
            MusicCollection musicCollection,
            MusicCollectionAuthorDto author,
            List<TrackShortcutDto> tracks) {
        MusicCollectionDataDto dto = new MusicCollectionDataDto();
        dto.setId(musicCollection.getId());
        dto.setAuthor(author);
        dto.setName(musicCollection.getName());
        dto.setDescription(musicCollection.getDescription());
        dto.setCoverReference(musicCollection.getImageReference());
        dto.setUploadDateTime(musicCollection.getUploadDateTime());
        dto.setNumberOfPlays(musicCollection.getNumberOfPlays());
        dto.setStatus(musicCollection.getAccessLevel().name());
        dto.setTracks(tracks);
        return dto;
    }

}
