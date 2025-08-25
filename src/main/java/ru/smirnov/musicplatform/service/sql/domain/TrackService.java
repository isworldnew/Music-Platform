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
import ru.smirnov.musicplatform.dto.domain.track.TrackAccessLevelUpdateDto;
import ru.smirnov.musicplatform.dto.domain.track.TrackDataDto;
import ru.smirnov.musicplatform.dto.domain.track.TrackToCreateDto;
import ru.smirnov.musicplatform.entity.auxiliary.enums.Role;
import ru.smirnov.musicplatform.entity.auxiliary.enums.TrackStatus;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.entity.domain.Track;
import ru.smirnov.musicplatform.exception.BadRequestException;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.mapper.TrackMapper;
import ru.smirnov.musicplatform.projection.CoArtistProjection;
import ru.smirnov.musicplatform.repository.domain.TrackRepository;
import ru.smirnov.musicplatform.service.SecurityContextService;
import ru.smirnov.musicplatform.service.minio.MinioService;
import ru.smirnov.musicplatform.service.sql.relation.CoArtistService;
import ru.smirnov.musicplatform.util.MinioPathUtil;
import ru.smirnov.musicplatform.validators.ArtistValidatorImproved;
import ru.smirnov.musicplatform.validators.TrackValidator;
import ru.smirnov.musicplatform.validators.enums.ContentType;
import ru.smirnov.musicplatform.validators.interfaces.FileValidator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TrackService {

    private final TrackRepository trackRepository;
    private final TrackMapper trackMapper;

    private final MinioService minioService;
    private final CoArtistService coArtistService;

    private final MinioPathUtil minioPathUtil;

    private final SecurityContextService securityContextService;

    private final List<FileValidator> fileValidators;
    private final ArtistValidatorImproved artistValidator;
    private final TrackValidator trackValidator;

    @Autowired
    public TrackService(
            TrackRepository trackRepository,
            TrackMapper trackMapper,
            MinioService minioService,
            MinioPathUtil minioPathUtil,
            SecurityContextService securityContextService,
            List<FileValidator> fileValidators,
            ArtistValidatorImproved artistValidator,
            CoArtistService coArtistService,
            TrackValidator trackValidator
    ) {
        this.trackRepository = trackRepository;
        this.trackMapper = trackMapper;
        this.minioService = minioService;
        this.minioPathUtil = minioPathUtil;
        this.securityContextService = securityContextService;
        this.fileValidators = fileValidators;
        this.artistValidator = artistValidator;
        this.coArtistService = coArtistService;
        this.trackValidator = trackValidator;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ResponseEntity<Long> uploadTrack(TrackToCreateDto dto) {

        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();

        Artist artist = this.artistValidator.distributorIsAbleToInteractWithThisArtist(tokenData.getEntityId(), dto.getAuthor());

        dto.getCoAuthors().forEach(coArtistId -> this.artistValidator.safelyGetById(coArtistId));

        // или ещё лучше: пара из id исполнителя и id трека в БД - они то не поменяются

        for (FileValidator fileValidator : this.fileValidators) fileValidator.validate(dto.getAudio(), ContentType.AUDIO);
        for (FileValidator fileValidator : this.fileValidators) fileValidator.validate(dto.getCover(), ContentType.IMAGE);

        Track trackToSave = this.trackMapper.createTrackEntity(dto, artist, null, "");
        this.trackRepository.save(trackToSave);



        if (!dto.getCoAuthors().isEmpty()) {
            if (dto.getCoAuthors().contains(dto.getAuthor()))
                throw new BadRequestException("Set of co-artists identifiers includes self-reference to the main artist with id=" + dto.getAuthor());

            dto.getCoAuthors().forEach(coArtistId -> this.coArtistService.save(trackToSave.getId(), coArtistId));
        }

        String objectName = dto.getAuthor() + "_" + trackToSave.getId();

        boolean coverIsAttached = (dto.getCover() != null && !dto.getCover().isEmpty());

        if (coverIsAttached) {
            trackToSave.setImageReference(this.minioPathUtil.generateFormattedReference(MinioBuckets.TRACK_COVER.getBucketName(), objectName));
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    minioService.uploadObjectWithMetadata(
                            MinioBuckets.TRACK_COVER.getBucketName(),
                            objectName,
                            dto.getCover(),
                            null
                    );
                }
            });
        }

        trackToSave.setAudiofileReference(this.minioPathUtil.generateFormattedReference(MinioBuckets.TRACK_AUDIO.getBucketName(), objectName));
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                minioService.uploadObjectWithMetadata(
                        MinioBuckets.TRACK_AUDIO.getBucketName(),
                        objectName,
                        dto.getAudio(),
                        null
                );
            }
        });

        this.trackRepository.save(trackToSave);

        return ResponseEntity.status(HttpStatus.CREATED).body(trackToSave.getId());

    }

    @Transactional
    public ResponseEntity<Void> listenTo(Long trackId) {
        Track track = this.trackValidator.safelyGetById(trackId);

        track.setNumberOfPlays(track.getNumberOfPlays() + 1);

        this.trackRepository.save(track);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ResponseEntity<TrackDataDto> setTrackAccessLevel(Long trackId, TrackAccessLevelUpdateDto dto) {

        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();

        Track track = this.trackValidator.safelyGetById(trackId);

        if (tokenData.getRole().equals("ROLE_" + Role.DISTRIBUTOR.name())) {
            // проверка на то, что данный трек относится к исполнителю, с которым у дистрибьютора есть ACTIVE-связь
            this.artistValidator.distributorIsAbleToInteractWithThisArtist(tokenData.getEntityId(), track.getArtist().getId());

            if (track.getStatus().equals(TrackStatus.BANNED))
                throw new ForbiddenException("Track is BANNED, distributor has no rights to change it");

            if (dto.getTrackAccessLevel().equals(TrackStatus.BANNED.name()))
                throw new ForbiddenException("Distributor has no rights to BAN track of it's artist");

        }

        // в данном методе может оказаться только DISTRIBUTOR или ADMIN,
        // поэтому то, что вне if - как бы действия, которые выполняются
        // в любом случае - то есть администратором


         track.setStatus(TrackStatus.valueOf(dto.getTrackAccessLevel()));

         this.trackRepository.save(track);

         return ResponseEntity.ok(
                 this.getTrackDataById(trackId, false).getBody()
         );
    }

    public ResponseEntity<TrackDataDto> getTrackDataById(Long trackId, boolean safeUpload) {

        // данный метод буду использовать в разных контекстах
        // администратор и исполнитель могут получить полную информацию о треке вне зависимости от его статуса
        // а вот гость и пользователь могут получить полную информацию только если трек доступен

        Track track = this.trackValidator.safelyGetById(trackId);

        List<CoArtistProjection> coArtists = this.coArtistService.getCoArtistProjections(trackId);

        TrackDataDto dto = this.trackMapper.trackEntityToTrackDataDto(track, coArtists, safeUpload);

        return ResponseEntity.ok(dto);

    }

}
