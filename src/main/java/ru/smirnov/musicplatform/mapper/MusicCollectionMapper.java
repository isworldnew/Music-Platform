package ru.smirnov.musicplatform.mapper;

import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.domain.MusicCollectionAuthorDto;
import ru.smirnov.musicplatform.dto.domain.album.MusicCollectionDataDto;
import ru.smirnov.musicplatform.dto.domain.album.AlbumToCreateDto;
import ru.smirnov.musicplatform.dto.domain.album.MusicCollectionToCreateDto;
import ru.smirnov.musicplatform.dto.domain.track.TrackShortcutDto;
import ru.smirnov.musicplatform.entity.audience.Admin;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.entity.auxiliary.hierarchy.MusicCollection;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.entity.domain.Chart;
import ru.smirnov.musicplatform.entity.domain.Playlist;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class MusicCollectionMapper {

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

    public Album albumToCreateDtoToAlbumEntity(AlbumToCreateDto dto, Artist artist) {
        Album album = new Album();
        album.setName(dto.getName());
        album.setDescription(dto.getDescription());
        album.setArtist(artist);
        album.setUploadDateTime(OffsetDateTime.now());
        // количество прослушиваний и уровень доступа по идее должны получить в БД значение по умолчанию
        // ссылку на обложку возможно будет установить только после сохранения записи и получения её id
        return album;
    }

    public Chart musicCollectionToCreateDtoToChartEntity(MusicCollectionToCreateDto dto, Admin admin) {
        Chart chart = new Chart();
        chart.setName(dto.getName());
        chart.setDescription(dto.getDescription());
        chart.setAdmin(admin);
        chart.setUploadDateTime(OffsetDateTime.now());
        return chart;
    }

    public Playlist musicCollectionToCreateDtoToAlbumEntity(MusicCollectionToCreateDto dto, User user) {
        Playlist playlist = new Playlist();
        playlist.setName(dto.getName());
        playlist.setDescription(dto.getDescription());
        playlist.setUser(user);
        playlist.setUploadDateTime(OffsetDateTime.now());
        return playlist;
    }

}
