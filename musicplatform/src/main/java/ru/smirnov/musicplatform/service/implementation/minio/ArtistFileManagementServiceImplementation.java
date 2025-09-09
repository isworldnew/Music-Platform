package ru.smirnov.musicplatform.service.implementation.minio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.config.MinioBuckets;
import ru.smirnov.musicplatform.dto.file.ImageFileRequest;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.exception.MinioException;
import ru.smirnov.musicplatform.precondition.abstraction.domain.ArtistPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.relation.DistributorByArtistPreconditionService;
import ru.smirnov.musicplatform.repository.domain.ArtistRepository;
import ru.smirnov.musicplatform.service.abstraction.minio.ArtistFileManagementService;
import ru.smirnov.musicplatform.service.abstraction.minio.MinioService;
import ru.smirnov.musicplatform.util.MinioPathUtil;

@Service
public class ArtistFileManagementServiceImplementation implements ArtistFileManagementService {

    private final MinioService minioService;

    private final ArtistPreconditionService artistPreconditionService;
    private final DistributorByArtistPreconditionService distributorByArtistPreconditionService;
    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistFileManagementServiceImplementation(
            MinioService minioService,
            ArtistPreconditionService artistPreconditionService,
            DistributorByArtistPreconditionService distributorByArtistPreconditionService,
            ArtistRepository artistRepository
    ) {
        this.minioService = minioService;
        this.artistPreconditionService = artistPreconditionService;
        this.distributorByArtistPreconditionService = distributorByArtistPreconditionService;
        this.artistRepository = artistRepository;
    }

    @Override
    @Transactional
    public void updateArtistCover(Long artistId, ImageFileRequest dto, DataForToken tokenData) {
        Artist artist = this.artistPreconditionService.getByIdIfExists(artistId);
        this.distributorByArtistPreconditionService.checkActiveRelationBetweenDistributorAndArtistExistence(tokenData.getEntityId(), artistId);

        boolean referenceExists = (artist.getImageReference() != null);
        boolean coverIsAttached = (dto.getImageFile() != null && !dto.getImageFile().isEmpty());

        if (!referenceExists && coverIsAttached) {
            String coverReference = MinioPathUtil.generateFormattedReference(MinioBuckets.ARTIST_COVER.getBucketName(), tokenData.getEntityId(), artistId);
            artist.setImageReference(coverReference);
            try {
                this.minioService.uploadObjectWithMetadata(
                        MinioBuckets.ARTIST_COVER.getBucketName(),
                        MinioPathUtil.extractObjectName(coverReference),
                        dto.getImageFile(),
                        null
                );
                this.artistRepository.save(artist);
                return;
            }
            catch (Exception e) {
                throw new MinioException("File download to MinIO failed");
            }
        }

        if (referenceExists && !coverIsAttached) {
            String coverReference = artist.getImageReference();
            artist.setImageReference(null);
            try {
                this.minioService.removeObject(
                        MinioBuckets.ARTIST_COVER.getBucketName(),
                        MinioPathUtil.extractObjectName(coverReference)
                );
                this.artistRepository.save(artist);
                return;
            }
            catch (Exception e) {
                throw new MinioException("File download to MinIO failed");
            }
        }

        if (referenceExists && coverIsAttached) {
            String coverReference = artist.getImageReference();
            try {
                this.minioService.replaceObjectInBucket(
                        MinioBuckets.ARTIST_COVER.getBucketName(),
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
