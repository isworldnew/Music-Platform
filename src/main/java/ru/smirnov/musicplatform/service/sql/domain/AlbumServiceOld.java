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
import ru.smirnov.musicplatform.entity.auxiliary.enums.MusicCollectionAccessLevel;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.mapper.old.MusicCollectionMapperOld;
import ru.smirnov.musicplatform.repository.domain.AlbumRepository;
import ru.smirnov.musicplatform.service.implementation.SecurityContextServiceImpl;
import ru.smirnov.musicplatform.service.minio.MinioService;
import ru.smirnov.musicplatform.service.sql.relation.TrackByAlbumServiceOld;
import ru.smirnov.musicplatform.util.MinioPathUtil;
import ru.smirnov.musicplatform.util.SetUtil;
import ru.smirnov.musicplatform.validators.old.AlbumValidator;
import ru.smirnov.musicplatform.validators.old.ArtistValidatorImproved;
import ru.smirnov.musicplatform.validators.old.TrackValidator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AlbumServiceOld {

    private final AlbumRepository albumRepository;
    private final MusicCollectionMapperOld musicCollectionMapper;

    private final SecurityContextServiceImpl securityContextService;

    private final MinioService minioService;
    private final MinioPathUtil minioPathUtil;

    private final ArtistValidatorImproved artistValidator;
    private final AlbumValidator albumValidator;
    private final TrackValidator trackValidator;

    private final TrackByAlbumServiceOld trackByAlbumService;
    private final TrackServiceOld trackService;

    @Autowired
    public AlbumServiceOld(
            AlbumRepository albumRepository,
            MusicCollectionMapperOld musicCollectionMapper,
            SecurityContextServiceImpl securityContextService,
            MinioService minioService,
            MinioPathUtil minioPathUtil,
            ArtistValidatorImproved artistValidator,
            AlbumValidator albumValidator,
            TrackValidator trackValidator,
            TrackByAlbumServiceOld trackByAlbumService,
            TrackServiceOld trackService
    ) {
        this.albumRepository = albumRepository;
        this.musicCollectionMapper = musicCollectionMapper;
        this.securityContextService = securityContextService;
        this.minioService = minioService;
        this.minioPathUtil = minioPathUtil;
        this.artistValidator = artistValidator;
        this.albumValidator = albumValidator;
        this.trackValidator = trackValidator;
        this.trackByAlbumService = trackByAlbumService;
        this.trackService = trackService;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ResponseEntity<Long> createAlbum(AlbumToCreateDto dto) {

        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();

        boolean tracksAttached = (dto.getTracks() != null && !dto.getTracks().isEmpty());
        boolean coverAttached = (dto.getCover() != null && !dto.getCover().isEmpty());

        Artist artist = null;

        // вот тут пройдут в принципе все нужные провеки на то, что:
        // [v] данный исполнитель существует
        // [v] данный дистрибьютор имеет ACTIVE-ную связь с данным исполнителем
        // [v] если передали какие-то треки, то все они существуют
        // [v] относятся к данному исполнителю (его собственные или он - соавтор)
        if (tracksAttached) {
            dto.getTracks().forEach(track -> this.trackValidator.safelyGetById(track));
            artist = this.artistValidator.distributorIsAbleToInteractWithThisArtist(tokenData.getEntityId(), dto.getArtistId(), dto.getTracks());
        }
        else
            artist = this.artistValidator.distributorIsAbleToInteractWithThisArtist(tokenData.getEntityId(), dto.getArtistId());

        // [v] дополнительно проверяю, что у данного исполнителя ещё нет альбома с таким названием
        this.albumValidator.checkAlbumNameUniquenessForArtist(dto.getName(), dto.getArtistId());

        Album album = this.musicCollectionMapper.albumToCreateDtoToAlbumEntity(dto, artist);

        // раннее сохранение, чтобы у нас был id альбома, который нужен
        // для создания ссылки на обложку и для сохранения треков в альбом
        this.albumRepository.save(album);

        if (tracksAttached)
            this.trackByAlbumService.addTracksToAlbum(album.getId(), dto.getTracks().stream().toList());


        if (coverAttached) {
            // ссылка будет составляться из artist_id и album_id
            String coverReference = this.minioPathUtil.generateFormattedReference(MinioBuckets.ALBUM_COVER.getBucketName(), artist.getId(), album.getId());

            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    minioService.uploadObjectWithMetadata(
                            MinioBuckets.ALBUM_COVER.getBucketName(),
                            minioPathUtil.extractObjectName(coverReference),
                            dto.getCover(),
                            null
                    );
                }
            });

            album.setImageReference(coverReference);
            this.albumRepository.save(album);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(album.getId());
    }

    @Transactional
    public ResponseEntity<Void> updateAlbum(MusicCollectionToUpdateDto dto) {

        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();

        // [v] проверка на то, что альбом с таким id вообще существует
        Album album = this.albumValidator.safelyGetById(dto.getMusicCollectionId());

        // [v] проверка на то, что данный дистрибьютор может взаимодействовать с данным альбомом
        this.artistValidator.distributorIsAbleToInteractWithThisArtist(tokenData.getEntityId(), album.getArtist().getId());

        // [v] проверка на то, что пришедшее имя либо уникально для альбомов данного исполнителя, либо принадлежит этому же альбому
        this.albumValidator.checkAlbumNameUniquenessForArtistDuringUpdate(dto.getName(), album.getArtist().getId(), album.getId());

        album.setName(dto.getName());
        album.setDescription(dto.getDescription());

        boolean coverAttached = (dto.getCover() != null && !dto.getCover().isEmpty());
        boolean referenceExists = (album.getImageReference() != null);

        if (coverAttached && referenceExists) {
            // перезаписать файл
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    minioService.replaceObjectInBucket(
                            MinioBuckets.ALBUM_COVER.getBucketName(),
                            minioPathUtil.extractObjectName(album.getImageReference()),
                            dto.getCover()
                    );
                }
            });
        }

        if (coverAttached && !referenceExists) {
            // записать ссылку и файл
            String coverReference = this.minioPathUtil.generateFormattedReference(MinioBuckets.ALBUM_COVER.getBucketName(), album.getArtist().getId(), album.getId());
            album.setImageReference(coverReference);
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    minioService.uploadObjectWithMetadata(
                            MinioBuckets.ALBUM_COVER.getBucketName(),
                            minioPathUtil.extractObjectName(coverReference),
                            dto.getCover(),
                            null
                    );
                }
            });
        }

        if (!coverAttached && referenceExists) {
            // удалить ссылку и удалить файл
            String coverReference = album.getImageReference();
            album.setImageReference(null);
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    minioService.removeObject(
                            MinioBuckets.ALBUM_COVER.getBucketName(),
                            minioPathUtil.extractObjectName(coverReference)
                    );
                }
            });
        }

        this.albumRepository.save(album);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Transactional
    public ResponseEntity<Void> updateAccessLevel(MusicCollectionAccessLevelUpdateDto dto) {

        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();

        // [v] проверка на то, что альбом с таким id вообще существует
        Album album = this.albumValidator.safelyGetById(dto.getMusicCollectionId());

        // [v] проверка на то, что данный дистрибьютор может взаимодействовать с данным альбомом
        this.artistValidator.distributorIsAbleToInteractWithThisArtist(tokenData.getEntityId(), album.getArtist().getId());

        album.setAccessLevel(MusicCollectionAccessLevel.valueOf(dto.getMusicCollectionAccessLevel()));

        this.albumRepository.save(album);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Transactional
    public ResponseEntity<Void> updateAlbumByTracks(MusicCollectionTracksDto dto) {

        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();

        // [v] проверка на то, что все переданные треки в принципе существуют
        for (Long trackId : dto.getTracks()) this.trackValidator.safelyGetById(trackId);

        // [v] проверка на то, что переданный альбом существует
        Album album = this.albumValidator.safelyGetById(dto.getMusicCollectionId());

        // [v] проверка на то, может ли данный дистрибьютор взаимодействовать с данным исполнителем
        // [v] проверка на то, относятся ли все переданные треки к исполнителю (собственные или соавторство)
        Artist artist = this.artistValidator.distributorIsAbleToInteractWithThisArtist(tokenData.getEntityId(), album.getArtist().getId(), dto.getTracks());

        Set<Long> tracksSetBeforeUpdate = album.getTracks().stream().map(track -> track.getTrack().getId()).collect(Collectors.toSet());

        Set<Long> tracksToAdd = SetUtil.findAddedValues(tracksSetBeforeUpdate, dto.getTracks());
        Set<Long> tracksToRemove = SetUtil.findRemovedValues(tracksSetBeforeUpdate, dto.getTracks());

        this.trackByAlbumService.addTracksToAlbum(album.getId(), tracksToAdd.stream().toList());
        this.trackByAlbumService.removeTracksFromAlbum(album.getId(), tracksToRemove.stream().toList());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    public ResponseEntity<MusicCollectionDataDto> getAlbumById(Long albumId) {

        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        Album album = this.albumValidator.safelyGetById(albumId);
        this.artistValidator.distributorIsAbleToInteractWithThisArtist(tokenData.getEntityId(), album.getArtist().getId());

        List<Long> tracks = album.getTracks().stream()
                .map(track -> track.getTrack().getId())
                .toList();

        List<TrackShortcutDto> trackShortcuts = this.trackService.getTracksShortcutData(tracks, false);

        MusicCollectionDataDto dto = this.musicCollectionMapper.musicCollectionToMusicCollectionDataDto(
                album, new MusicCollectionAuthorDto(album.getArtist().getId(), album.getArtist().getName()), trackShortcuts
        );

        return ResponseEntity.ok(dto);
    }

    @Transactional
    public ResponseEntity<MusicCollectionDataDto> getAlbumByIdSafely(Long albumId) {

        Album album = this.albumValidator.safelyGetById(albumId);

        if (!album.getAccessLevel().isAvailable())
            throw new ForbiddenException("Album with id=" + albumId + " is PRIVATE");

        album.setNumberOfPlays(album.getNumberOfPlays() + 1);

        this.albumRepository.save(album);

        List<Long> tracks = album.getTracks().stream()
                .map(track -> track.getTrack().getId())
                .toList();

        List<TrackShortcutDto> trackShortcuts = this.trackService.getTracksShortcutData(tracks, true);

        MusicCollectionDataDto dto = this.musicCollectionMapper.musicCollectionToMusicCollectionDataDto(
                album, new MusicCollectionAuthorDto(album.getArtist().getId(), album.getArtist().getName()), trackShortcuts
        );

        return ResponseEntity.ok(dto);
    }

}
