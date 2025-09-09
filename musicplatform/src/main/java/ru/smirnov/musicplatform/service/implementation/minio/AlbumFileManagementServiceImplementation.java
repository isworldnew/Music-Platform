package ru.smirnov.musicplatform.service.implementation.minio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.config.MinioBuckets;
import ru.smirnov.musicplatform.dto.file.ImageFileRequest;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.exception.MinioException;
import ru.smirnov.musicplatform.precondition.abstraction.domain.AlbumPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.relation.DistributorByArtistPreconditionService;
import ru.smirnov.musicplatform.repository.domain.AlbumRepository;
import ru.smirnov.musicplatform.service.abstraction.minio.AlbumFileManagementService;
import ru.smirnov.musicplatform.service.abstraction.minio.MinioService;
import ru.smirnov.musicplatform.util.MinioPathUtil;

@Service
public class AlbumFileManagementServiceImplementation implements AlbumFileManagementService {

    private final MinioService minioService;

    private final AlbumPreconditionService albumPreconditionService;
    private final DistributorByArtistPreconditionService distributorByArtistPreconditionService;
    private final AlbumRepository albumRepository;

    @Autowired
    public AlbumFileManagementServiceImplementation(
            MinioService minioService,
            AlbumPreconditionService albumPreconditionService,
            DistributorByArtistPreconditionService distributorByArtistPreconditionService,
            AlbumRepository albumRepository
    ) {
        this.minioService = minioService;
        this.albumPreconditionService = albumPreconditionService;
        this.distributorByArtistPreconditionService = distributorByArtistPreconditionService;
        this.albumRepository = albumRepository;
    }

    @Override
    @Transactional
    public void updateAlbumCover(Long albumId, ImageFileRequest dto, DataForToken tokenData) {
        Album album = this.albumPreconditionService.getByIdIfExists(albumId);
        this.distributorByArtistPreconditionService.checkActiveRelationBetweenDistributorAndArtistExistence(tokenData.getEntityId(), album.getArtist().getId());

        boolean referenceExists = (album.getImageReference() != null);
        boolean coverIsAttached = (dto.getImageFile() != null && !dto.getImageFile().isEmpty());

        if (!referenceExists && coverIsAttached) {
            String coverReference = MinioPathUtil.generateFormattedReference(MinioBuckets.ALBUM_COVER.getBucketName(), tokenData.getEntityId(), albumId);
            album.setImageReference(coverReference);
            try {
                this.minioService.uploadObjectWithMetadata(
                        MinioBuckets.ALBUM_COVER.getBucketName(),
                        MinioPathUtil.extractObjectName(coverReference),
                        dto.getImageFile(),
                        null
                );
                this.albumRepository.save(album);
                return;
            }
            catch (Exception e) {
                throw new MinioException("File download to MinIO failed");
            }
        }

        if (referenceExists && !coverIsAttached) {
            String coverReference = album.getImageReference();
            album.setImageReference(null);
            try {
                this.minioService.removeObject(
                        MinioBuckets.ALBUM_COVER.getBucketName(),
                        MinioPathUtil.extractObjectName(coverReference)
                );
                this.albumRepository.save(album);
                return;
            }
            catch (Exception e) {
                throw new MinioException("File download to MinIO failed");
            }
        }

        if (referenceExists && coverIsAttached) {
            String coverReference = album.getImageReference();
            try {
                this.minioService.replaceObjectInBucket(
                        MinioBuckets.ALBUM_COVER.getBucketName(),
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
