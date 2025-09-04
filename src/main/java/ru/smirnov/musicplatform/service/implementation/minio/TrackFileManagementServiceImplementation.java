package ru.smirnov.musicplatform.service.implementation.minio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.config.MinioBuckets;
import ru.smirnov.musicplatform.dto.file.AudioFileRequest;
import ru.smirnov.musicplatform.dto.file.ImageFileRequest;
import ru.smirnov.musicplatform.entity.domain.Track;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.exception.MinioException;
import ru.smirnov.musicplatform.precondition.abstraction.domain.TrackPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.relation.DistributorByArtistPreconditionService;
import ru.smirnov.musicplatform.repository.domain.TrackRepository;
import ru.smirnov.musicplatform.service.abstraction.minio.MinioService;
import ru.smirnov.musicplatform.service.abstraction.minio.TrackFileManagementService;
import ru.smirnov.musicplatform.util.MinioPathUtil;

@Service
public class TrackFileManagementServiceImplementation implements TrackFileManagementService {

    private final MinioService minioService;

    private final TrackPreconditionService trackPreconditionService;
    private final DistributorByArtistPreconditionService distributorByArtistPreconditionService;
    private final TrackRepository trackRepository;

    @Autowired
    public TrackFileManagementServiceImplementation(
            MinioService minioService,
            TrackPreconditionService trackPreconditionService,
            DistributorByArtistPreconditionService distributorByArtistPreconditionService,
            TrackRepository trackRepository
    ) {
        this.minioService = minioService;
        this.trackPreconditionService = trackPreconditionService;
        this.distributorByArtistPreconditionService = distributorByArtistPreconditionService;
        this.trackRepository = trackRepository;
    }

    @Override
    @Transactional
    public void updateTrackCover(Long trackId, ImageFileRequest dto, DataForToken tokenData) {
        Track track = this.trackPreconditionService.getByIdIfExists(trackId);
        this.distributorByArtistPreconditionService.checkActiveRelationBetweenDistributorAndArtistExistence(tokenData.getEntityId(), track.getArtist().getId());

        boolean referenceExists = (track.getImageReference() != null);
        boolean coverAttached = (dto.getImageFile() != null && dto.getImageFile().isEmpty());

        System.out.println("действие");

        if (!referenceExists && coverAttached) {
            System.out.println("действие 1");
            String coverReference = MinioPathUtil.generateFormattedReference(MinioBuckets.TRACK_COVER.getBucketName(), track.getArtist().getId(), trackId);
            track.setImageReference(coverReference);
            try {
                this.minioService.uploadObjectWithMetadata(
                        MinioBuckets.TRACK_COVER.getBucketName(),
                        MinioPathUtil.extractObjectName(coverReference),
                        dto.getImageFile(),
                        null

                );
                this.trackRepository.save(track);
                return;
            }
            catch (Exception e) {
                throw new MinioException("File download to MinIO failed");
            }
        }

        if (referenceExists && !coverAttached) {
            System.out.println("действие 2");
            String coverReference = track.getImageReference();
            track.setImageReference(null);
            try {
                this.minioService.removeObject(
                        MinioBuckets.TRACK_COVER.getBucketName(),
                        MinioPathUtil.extractObjectName(coverReference)
                );
                this.trackRepository.save(track);
                return;
            }
            catch (Exception e) {
                throw new MinioException("File download to MinIO failed");
            }
        }

        if (referenceExists && coverAttached) {
            System.out.println("действие 3");
            String coverReference = track.getImageReference();
            try {
                this.minioService.replaceObjectInBucket(
                        MinioBuckets.TRACK_COVER.getBucketName(),
                        MinioPathUtil.extractObjectName(coverReference),
                        dto.getImageFile()
                );
            }
            catch (Exception e) {
                throw new MinioException("File download to MinIO failed");
            }
        }
    }

    @Override
    @Transactional
    public void updateTrackAudio(Long trackId, AudioFileRequest dto, DataForToken tokenData) {
        Track track = this.trackPreconditionService.getByIdIfExists(trackId);
        this.distributorByArtistPreconditionService.checkActiveRelationBetweenDistributorAndArtistExistence(tokenData.getEntityId(), track.getArtist().getId());

        if (track.getStatus().isAvailable())
            throw new ForbiddenException("Track (id=" + trackId + ") is PUBLIC: audiofile update is not allowed");

        boolean referenceExists = (track.getImageReference() != null);
        boolean audioAttached = (dto.getAudioFile() != null && dto.getAudioFile().isEmpty());

        if (!referenceExists && audioAttached) {
            String audioReference = MinioPathUtil.generateFormattedReference(MinioBuckets.TRACK_AUDIO.getBucketName(), track.getArtist().getId(), trackId);
            track.setAudiofileReference(audioReference);
            try {
                this.minioService.uploadObjectWithMetadata(
                        MinioBuckets.TRACK_AUDIO.getBucketName(),
                        MinioPathUtil.extractObjectName(audioReference),
                        dto.getAudioFile(),
                        null
                );
                this.trackRepository.save(track);
                return;
            }
            catch (Exception e) {
                throw new MinioException("File download to MinIO failed");
            }
        }

        if (referenceExists && !audioAttached) {
            String audioReference = track.getAudiofileReference();
            track.setAudiofileReference(null);
            try {
                this.minioService.removeObject(
                        MinioBuckets.TRACK_AUDIO.getBucketName(),
                        MinioPathUtil.extractObjectName(audioReference)
                );
                this.trackRepository.save(track);
                return;
            }
            catch (Exception e) {
                throw new MinioException("File download to MinIO failed");
            }
        }

        if (referenceExists && audioAttached) {
            String audioReference = track.getAudiofileReference();
            try {
                this.minioService.replaceObjectInBucket(
                        MinioBuckets.TRACK_AUDIO.getBucketName(),
                        MinioPathUtil.extractObjectName(audioReference),
                        dto.getAudioFile()
                );
            }
            catch (Exception e) {
                throw new MinioException("File download to MinIO failed");
            }
        }
    }
}
