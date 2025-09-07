package ru.smirnov.musicplatform.service.implementation.minio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.config.MinioBuckets;
import ru.smirnov.musicplatform.dto.file.ImageFileRequest;
import ru.smirnov.musicplatform.entity.domain.Playlist;
import ru.smirnov.musicplatform.exception.MinioException;
import ru.smirnov.musicplatform.precondition.abstraction.domain.PlaylistPreconditionService;
import ru.smirnov.musicplatform.repository.domain.PlaylistRepository;
import ru.smirnov.musicplatform.service.abstraction.minio.MinioService;
import ru.smirnov.musicplatform.service.abstraction.minio.PlaylistFileManagementService;
import ru.smirnov.musicplatform.util.MinioPathUtil;

@Service
public class PlaylistFileManagementServiceImplementation implements PlaylistFileManagementService {

    private final MinioService minioService;

    private final PlaylistPreconditionService playlistPreconditionService;
    private final PlaylistRepository playlistRepository;

    @Autowired
    public PlaylistFileManagementServiceImplementation(
            MinioService minioService,
            PlaylistPreconditionService playlistPreconditionService,
            PlaylistRepository playlistRepository
    ) {
        this.minioService = minioService;
        this.playlistPreconditionService = playlistPreconditionService;
        this.playlistRepository = playlistRepository;
    }

    @Override
    @Transactional
    public void updatePlaylistCover(Long playlistId, ImageFileRequest dto, DataForToken tokenData) {
        Playlist playlist = this.playlistPreconditionService.existsAndBelongToUser(playlistId, tokenData.getEntityId());

        boolean referenceExists = (playlist.getImageReference() != null);
        boolean coverIsAttached = (dto.getImageFile() != null && !dto.getImageFile().isEmpty());

        if (!referenceExists && coverIsAttached) {
            String coverReference = MinioPathUtil.generateFormattedReference(MinioBuckets.PLAYLIST_COVER.getBucketName(), tokenData.getEntityId(), playlistId);
            playlist.setImageReference(coverReference);
            try {
                this.minioService.uploadObjectWithMetadata(
                        MinioBuckets.PLAYLIST_COVER.getBucketName(),
                        MinioPathUtil.extractObjectName(coverReference),
                        dto.getImageFile(),
                        null
                );
                this.playlistRepository.save(playlist);
                return;
            }
            catch (Exception e) {
                throw new MinioException("File download to MinIO failed");
            }
        }

        if (referenceExists && !coverIsAttached) {
            String coverReference = playlist.getImageReference();
            playlist.setImageReference(null);
            try {
                this.minioService.removeObject(
                        MinioBuckets.PLAYLIST_COVER.getBucketName(),
                        MinioPathUtil.extractObjectName(coverReference)
                );
                this.playlistRepository.save(playlist);
                return;
            }
            catch (Exception e) {
                throw new MinioException("File download to MinIO failed");
            }
        }

        if (referenceExists && coverIsAttached) {
            String coverReference = playlist.getImageReference();
            try {
                this.minioService.replaceObjectInBucket(
                        MinioBuckets.PLAYLIST_COVER.getBucketName(),
                        MinioPathUtil.extractObjectName(coverReference),
                        dto.getImageFile()
                );
            }
            catch (Exception e) {
                throw new MinioException("File download to MinIO failed");
            }
        }

    }

}
