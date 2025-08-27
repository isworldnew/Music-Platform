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
import ru.smirnov.musicplatform.entity.auxiliary.enums.Role;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.mapper.MusicCollectionMapper;
import ru.smirnov.musicplatform.repository.domain.AlbumRepository;
import ru.smirnov.musicplatform.service.SecurityContextService;
import ru.smirnov.musicplatform.service.minio.MinioService;
import ru.smirnov.musicplatform.service.sql.relation.TrackByAlbumService;
import ru.smirnov.musicplatform.util.MinioPathUtil;
import ru.smirnov.musicplatform.util.SetUtil;
import ru.smirnov.musicplatform.validators.AlbumValidator;
import ru.smirnov.musicplatform.validators.ArtistValidatorImproved;
import ru.smirnov.musicplatform.validators.TrackValidator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final MusicCollectionMapper musicCollectionMapper;

    private final SecurityContextService securityContextService;

    private final MinioService minioService;
    private final MinioPathUtil minioPathUtil;

    private final ArtistValidatorImproved artistValidator;
    private final AlbumValidator albumValidator;
    private final TrackValidator trackValidator;

    private final TrackByAlbumService trackByAlbumService;
    private final TrackService trackService;

    @Autowired
    public AlbumService(
            AlbumRepository albumRepository,
            MusicCollectionMapper musicCollectionMapper,
            SecurityContextService securityContextService,
            MinioService minioService,
            MinioPathUtil minioPathUtil,
            ArtistValidatorImproved artistValidator,
            AlbumValidator albumValidator,
            TrackValidator trackValidator,
            TrackByAlbumService trackByAlbumService,
            TrackService trackService
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
    public ResponseEntity<Long> createAlbum(MusicCollectionToCreateDto dto) {

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
            artist = this.artistValidator.distributorIsAbleToInteractWithThisArtist(tokenData.getEntityId(), dto.getCreatorId(), dto.getTracks());
        }
        else
            artist = this.artistValidator.distributorIsAbleToInteractWithThisArtist(tokenData.getEntityId(), dto.getCreatorId());

        // [v] дополнительно проверяю, что у данного исполнителя ещё нет альбома с таким названием
        this.albumValidator.checkAlbumNameUniquenessForArtist(dto.getName(), dto.getCreatorId());

        Album album = this.musicCollectionMapper.musicCollectionToCreateDtoToAlbumEntity(dto, artist);

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
    public ResponseEntity<MusicCollectionDataDto> updateAlbum(MusicCollectionToUpdateDto dto) {

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

//            io.minio.errors.ErrorResponseException: This copy request is illegal because it is trying to copy an object to itself without changing the object's metadata, storage class, website redirect location or encryption attributes.
//            at io.minio.S3Base$1.onResponse(S3Base.java:789) ~[minio-8.5.17.jar:8.5.17]
//            at io.minio.S3Base$1.onResponse(S3Base.java:625) ~[minio-8.5.17.jar:8.5.17]
//            at okhttp3.internal.connection.RealCall$AsyncCall.run(RealCall.kt:519) ~[okhttp-4.12.0.jar:na]

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

        return ResponseEntity.status(HttpStatus.OK).body(this.getAlbumById(album.getId()).getBody());
    }

    @Transactional
    public ResponseEntity<MusicCollectionDataDto> updateAccessLevel(MusicCollectionAccessLevelUpdateDto dto) {

        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();

        // [v] проверка на то, что альбом с таким id вообще существует
        Album album = this.albumValidator.safelyGetById(dto.getMusicCollectionId());

        // [v] проверка на то, что данный дистрибьютор может взаимодействовать с данным альбомом
        this.artistValidator.distributorIsAbleToInteractWithThisArtist(tokenData.getEntityId(), album.getArtist().getId());

        album.setAccessLevel(MusicCollectionAccessLevel.valueOf(dto.getMusicCollectionAccessLevel()));

        this.albumRepository.save(album);

        return ResponseEntity.status(HttpStatus.OK).body(this.getAlbumById(album.getId()).getBody());
    }

    @Transactional
    public ResponseEntity<MusicCollectionDataDto> updateAlbumByTracks(MusicCollectionTracksDto dto) {

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

        return ResponseEntity.status(HttpStatus.OK).body(this.getAlbumById(album.getId()).getBody());
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
