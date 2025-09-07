package ru.smirnov.musicplatform.service.implementation.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionAccessLevelRequest;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionRequest;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.entity.auxiliary.enums.MusicCollectionAccessLevel;
import ru.smirnov.musicplatform.entity.domain.Playlist;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.mapper.abstraction.PlaylistMapper;
import ru.smirnov.musicplatform.precondition.abstraction.domain.PlaylistPreconditionService;
import ru.smirnov.musicplatform.repository.audience.UserRepository;
import ru.smirnov.musicplatform.repository.domain.PlaylistRepository;
import ru.smirnov.musicplatform.service.abstraction.domain.PlaylistService;

@Service
public class PlaylistServiceImplementation implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final PlaylistMapper playlistMapper;
    private final PlaylistPreconditionService playlistPreconditionService;

    private final UserRepository userRepository;

    @Autowired
    public PlaylistServiceImplementation(
            PlaylistRepository playlistRepository,
            PlaylistMapper playlistMapper,
            PlaylistPreconditionService playlistPreconditionService,
            UserRepository userRepository
    ) {
        this.playlistRepository = playlistRepository;
        this.playlistMapper = playlistMapper;
        this.playlistPreconditionService = playlistPreconditionService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Long createPlaylist(MusicCollectionRequest dto, DataForToken tokenData) {
        User user = this.userRepository.findById(tokenData.getEntityId()).orElseThrow(
                () -> new ForbiddenException("User's business-data wasn't found by users's id in token")
        );
        this.playlistPreconditionService.existsByNameAndUserId(dto.getName(), user.getId());

        Playlist playlist = this.playlistMapper.musicCollectionRequestToPlaylistEntity(dto, user);
        this.playlistRepository.save(playlist);

        return playlist.getId();
    }

    @Override
    @Transactional
    public void updatePlaylist(Long playlistId, MusicCollectionRequest dto, DataForToken tokenData) {
        /*
        Проверки, что:
        [v] Плейлист существует
        [v] Плейлист относится к данному пользователю
        [v] Плейлист либо не обновляет имя, либо новое имя - уникально среди плейлистов пользователя
        */
        User user = this.userRepository.findById(tokenData.getEntityId()).orElseThrow(
                () -> new ForbiddenException("User's business-data wasn't found by users's id in token")
        );
        Playlist playlist = this.playlistPreconditionService.getByIdIfExistsAndNameIsUnique(playlistId, user.getId(), dto.getName());

        playlist.setName(dto.getName());
        playlist.setDescription(dto.getDescription());

        this.playlistRepository.save(playlist);
    }

    @Override
    @Transactional
    public void updatePlaylistAccessLevel(Long playlistId, MusicCollectionAccessLevelRequest dto, DataForToken tokenData) {
        /*
        Проверки, что:
        [v] Плейлист существует
        [v] Плейлист относится к данному пользователю
        */
        User user = this.userRepository.findById(tokenData.getEntityId()).orElseThrow(
                () -> new ForbiddenException("User's business-data wasn't found by users's id in token")
        );
        Playlist playlist = this.playlistPreconditionService.existsAndBelongToUser(playlistId, tokenData.getEntityId());

        playlist.setAccessLevel(MusicCollectionAccessLevel.valueOf(dto.getAccessLevel()));
        this.playlistRepository.save(playlist);
    }

    @Override
    public void deletePlaylist(Long playlistId, DataForToken tokenData) {
        Playlist playlist = this.playlistPreconditionService.existsAndBelongToUser(playlistId, tokenData.getEntityId());
        this.playlistRepository.deleteById(playlistId);
    }
}
