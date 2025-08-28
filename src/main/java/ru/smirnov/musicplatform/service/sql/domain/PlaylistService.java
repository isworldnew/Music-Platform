package ru.smirnov.musicplatform.service.sql.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.config.MinioBuckets;
import ru.smirnov.musicplatform.dto.domain.MusicCollectionAuthorDto;
import ru.smirnov.musicplatform.dto.domain.album.*;
import ru.smirnov.musicplatform.dto.domain.track.TrackShortcutDto;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.entity.auxiliary.enums.MusicCollectionAccessLevel;
import ru.smirnov.musicplatform.entity.domain.Playlist;
import ru.smirnov.musicplatform.entity.domain.Track;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.mapper.MusicCollectionMapper;
import ru.smirnov.musicplatform.repository.audience.UserRepository;
import ru.smirnov.musicplatform.repository.domain.PlaylistRepository;
import ru.smirnov.musicplatform.service.SecurityContextService;
import ru.smirnov.musicplatform.service.minio.MinioService;
import ru.smirnov.musicplatform.service.sql.relation.TrackByPlaylistService;
import ru.smirnov.musicplatform.util.MinioPathUtil;
import ru.smirnov.musicplatform.util.SetUtil;
import ru.smirnov.musicplatform.validators.PlaylistValidator;
import ru.smirnov.musicplatform.validators.TrackValidator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final MusicCollectionMapper musicCollectionMapper;

    private final SecurityContextService securityContextService;

    private final MinioService minioService;
    private final MinioPathUtil minioPathUtil;

    private final TrackService trackService;
    private final TrackByPlaylistService trackByPlaylistService;

    private final PlaylistValidator playlistValidator;
    private final TrackValidator trackValidator;

    private final UserRepository userRepository;

    @Autowired
    public PlaylistService(
            PlaylistRepository playlistRepository,
            MusicCollectionMapper musicCollectionMapper,
            SecurityContextService securityContextService,
            MinioService minioService,
            MinioPathUtil minioPathUtil,
            TrackService trackService,
            TrackByPlaylistService trackByPlaylistService,
            PlaylistValidator playlistValidator,
            TrackValidator trackValidator,
            UserRepository userRepository
    ) {
        this.playlistRepository = playlistRepository;
        this.musicCollectionMapper = musicCollectionMapper;
        this.securityContextService = securityContextService;
        this.minioService = minioService;
        this.minioPathUtil = minioPathUtil;
        this.trackService = trackService;
        this.trackByPlaylistService = trackByPlaylistService;
        this.playlistValidator = playlistValidator;
        this.trackValidator = trackValidator;
        this.userRepository = userRepository;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ResponseEntity<Long> createPlaylist(MusicCollectionToCreateDto dto) {

        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        User user = this.userRepository.findById(tokenData.getEntityId()).orElseThrow(
                () -> new ForbiddenException("User's business-data wasn't found by users's id in token")
        );

        // [v] для одного пользователя имена его плейлистов должны быть уникальны
        this.playlistValidator.checkPlaylistExistenceByNameAndUserId(dto.getName(), user.getId());

        boolean coverAttached = (dto.getCover() != null && !dto.getCover().isEmpty());
        boolean tracksAttached = (dto.getTracks() != null && !dto.getTracks().isEmpty());

        Playlist playlist = this.playlistRepository.save(this.musicCollectionMapper.musicCollectionToCreateDtoToAlbumEntity(dto, user));

        this.playlistRepository.save(playlist);

        if (coverAttached) {
            String coverReference = this.minioPathUtil.generateFormattedReference(MinioBuckets.PLAYLIST_COVER.getBucketName(), user.getId(), playlist.getId());
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    minioService.uploadObjectWithMetadata(
                            MinioBuckets.PLAYLIST_COVER.getBucketName(),
                            minioPathUtil.extractObjectName(coverReference),
                            dto.getCover(),
                            null
                    );
                }
            });
            playlist.setImageReference(coverReference);
            this.playlistRepository.save(playlist);
        }

        if (tracksAttached) {
            // [v] каждый из треков должен существовать и должен быть PUBLIC
            List<Track> tracks = dto.getTracks().stream().map(trackId -> this.trackValidator.safelyGetByIdWithActiveStatus(trackId)).toList();
            this.trackByPlaylistService.save(playlist.getId(), tracks.stream().map(track -> track.getId()).toList());
        }

        return ResponseEntity.ok(playlist.getId());
    }

    @Transactional
    public ResponseEntity<Void> updatePlaylist(MusicCollectionToUpdateDto dto) {

        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        User user = this.userRepository.findById(tokenData.getEntityId()).orElseThrow(
                () -> new ForbiddenException("User's business-data wasn't found by users's id in token")
        );

        // одна эта строка проверяет, что:
        // [v] плейлист с таким id существует
        // [v] данный пользователь имеет право управлять этим плейлистом
        // [v] что переданное в dto имя плейлиста либо относится к найденному плейлисту, либо уникально среди плейлистов данного пользователя
        Playlist playlist = this.playlistValidator.getPlaylistBelongingToUserForUpdate(dto.getMusicCollectionId(), user.getId(), dto.getName());

        playlist.setName(dto.getName());
        playlist.setDescription(dto.getDescription());


        boolean coverAttached = (dto.getCover() != null && !dto.getCover().isEmpty());
        boolean referenceExists = (playlist.getImageReference() != null);

        if (coverAttached && referenceExists) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    minioService.replaceObjectInBucket(
                            MinioBuckets.PLAYLIST_COVER.getBucketName(),
                            minioPathUtil.extractObjectName(playlist.getImageReference()),
                            dto.getCover()
                    );
                }
            });
        }

        if (!coverAttached && referenceExists) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    minioService.removeObject(
                            MinioBuckets.PLAYLIST_COVER.getBucketName(),
                            minioPathUtil.extractObjectName(playlist.getImageReference())
                    );
                }
            });
            playlist.setImageReference(null);
        }

        if (coverAttached && !referenceExists) {
            String coverReference = this.minioPathUtil.generateFormattedReference(MinioBuckets.PLAYLIST_COVER.getBucketName(), user.getId(), playlist.getId());
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    minioService.uploadObjectWithMetadata(
                            MinioBuckets.PLAYLIST_COVER.getBucketName(),
                            minioPathUtil.extractObjectName(coverReference),
                            dto.getCover(),
                            null
                    );
                }
            });
            playlist.setImageReference(coverReference);
        }

        this.playlistRepository.save(playlist);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Transactional
    public ResponseEntity<Void> updatePlaylistAccessLevel(MusicCollectionAccessLevelUpdateDto dto) {

        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        User user = this.userRepository.findById(tokenData.getEntityId()).orElseThrow(
                () -> new ForbiddenException("User's business-data wasn't found by users's id in token")
        );

        // одна эта строка проверяет, что:
        // [v] плейлист с таким id существует
        // [v] данный пользователь имеет право управлять этим плейлистом
        Playlist playlist = this.playlistValidator.getPlaylistBelongingToUser(dto.getMusicCollectionId(), user.getId());

        playlist.setAccessLevel(MusicCollectionAccessLevel.valueOf(dto.getMusicCollectionAccessLevel()));

        this.playlistRepository.save(playlist);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Transactional
    public ResponseEntity<Void> updatePlaylistByTracks(MusicCollectionTracksDto dto) {

        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        User user = this.userRepository.findById(tokenData.getEntityId()).orElseThrow(
                () -> new ForbiddenException("User's business-data wasn't found by users's id in token")
        );

        // одна эта строка проверяет, что:
        // [v] плейлист с таким id существует
        // [v] данный пользователь имеет право управлять этим плейлистом
        Playlist playlist = this.playlistValidator.getPlaylistBelongingToUser(dto.getMusicCollectionId(), user.getId());

        // [v] проверка на то, что все переданные треки в принципе существуют и что они доступны
        for (Long trackId : dto.getTracks()) this.trackValidator.safelyGetByIdWithActiveStatus(trackId);

        Set<Long> tracksSetBeforeUpdate = playlist.getTracks().stream().map(track -> track.getTrack().getId()).collect(Collectors.toSet());

        Set<Long> addedTracks = SetUtil.findAddedValues(tracksSetBeforeUpdate, dto.getTracks());
        Set<Long> removedTracks = SetUtil.findRemovedValues(tracksSetBeforeUpdate, dto.getTracks());

        this.trackByPlaylistService.save(playlist.getId(), addedTracks.stream().toList());
        this.trackByPlaylistService.remove(playlist.getId(), removedTracks.stream().toList());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Transactional
    public ResponseEntity<MusicCollectionDataDto> getPlaylistById(Long playlistId) {

        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        User user = this.userRepository.findById(tokenData.getEntityId()).orElseThrow(
                () -> new ForbiddenException("User's business-data wasn't found by users's id in token")
        );

        Playlist playlist = this.playlistValidator.getPlaylistByIdSafely(playlistId);
        playlist.setNumberOfPlays(playlist.getNumberOfPlays() + 1);
        this.playlistRepository.save(playlist);

        List<Long> tracks = playlist.getTracks().stream()
                .map(track -> track.getTrack().getId())
                .toList();

        List<TrackShortcutDto> shortcuts = this.trackService.getTracksShortcutData(tracks, true);

        MusicCollectionDataDto dto = this.musicCollectionMapper.musicCollectionToMusicCollectionDataDto(
                playlist,
                new MusicCollectionAuthorDto(user.getId(), user.getAccount().getUsername()),
                shortcuts
        );

        return ResponseEntity.ok(dto);
    }
}
