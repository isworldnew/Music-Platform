package ru.smirnov.musicplatform.finder.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionShortcutResponse;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.finder.abstraction.AlbumFinderService;
import ru.smirnov.musicplatform.mapper.abstraction.AlbumMapper;
import ru.smirnov.musicplatform.repository.audience.UserRepository;
import ru.smirnov.musicplatform.repository.domain.finder.AlbumFinderRepository;

import java.util.List;

@Service
public class AlbumFinderServiceImplementation implements AlbumFinderService {

    private final AlbumFinderRepository albumFinderRepository;
    private final AlbumMapper albumMapper;

    private final UserRepository userRepository;

    @Autowired
    public AlbumFinderServiceImplementation(AlbumFinderRepository albumFinderRepository, AlbumMapper albumMapper, UserRepository userRepository) {
        this.albumFinderRepository = albumFinderRepository;
        this.albumMapper = albumMapper;
        this.userRepository = userRepository;
    }


    @Override
    public List<MusicCollectionShortcutResponse> globalAlbumSearch(String searchRequest) {
        /*
        guest ищет альбомы по всей системе
        он может получить только PUBLIC-альбомы
        */
        List<Album> albums = this.albumFinderRepository.searchAlbums(searchRequest);
        List<MusicCollectionShortcutResponse> albumShortcuts = albums.stream()
                .filter(album -> album.getAccessLevel().isAvailable())
                .map(album -> this.albumMapper.albumEntityToMusicCollectionShortcutResponse(album))
                .toList();

        return albumShortcuts;
    }

    @Override
    public List<MusicCollectionShortcutResponse> globalAlbumSearch(String searchRequest, DataForToken tokenData) {
        /*
        user ищет альбомы по всей системе
        он может получить: [PUBLIC-альбом, если он не сохранён] ИЛИ [альбом с любым уровнем доступа, если он сохранён]
        */
        User user = this.userRepository.findById(tokenData.getEntityId()).orElseThrow(
                () -> new ForbiddenException("User's business-data wasn't found by users's id in token")
        );
        // получаю все альбомы, существующие в системе
        List<Album> albums = this.albumFinderRepository.searchAlbums(searchRequest);

        // получаю все альбомы, сохранённые данным пользователем
        List<Long> albumsSavedByUser = user.getSavedAlbums().stream().map(savedAlbum -> savedAlbum.getAlbum().getId()).toList();

        // проверяю каждый альбом из найденных в системе: или сохранён, или публичный (ну ещё возможен вариант, что и то, и то)
        albums = albums.stream()
                .filter(album -> albumsSavedByUser.contains(album.getId()) || album.getAccessLevel().isAvailable())
                .toList();

        return albums.stream()
                .map(
                        album -> this.albumMapper.albumEntityToMusicCollectionShortcutResponse(album, albumsSavedByUser.contains(album.getId()))
                )
                .toList();
    }

    @Override
    public List<MusicCollectionShortcutResponse> savedAlbumSearch(String searchRequest, DataForToken tokenData) {
        /*
        user ищет альбомы среди сохранённых
        он может получить: только сохранённые альбомы с любым уровнем доступа
        */
        List<Album> albums = this.albumFinderRepository.searchSavedAlbums(searchRequest, tokenData.getEntityId());

        List<MusicCollectionShortcutResponse> albumShortcuts = albums.stream()
                .map(album -> this.albumMapper.albumEntityToMusicCollectionShortcutResponse(album, true))
                .toList();

        return albumShortcuts;
    }

    // поиск альбомов у исполнителя (user или distributor)
    // + глянь комментарии в интерфейсе этого сервиса
}
