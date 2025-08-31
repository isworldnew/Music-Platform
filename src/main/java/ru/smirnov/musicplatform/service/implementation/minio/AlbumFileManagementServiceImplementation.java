package ru.smirnov.musicplatform.service.implementation.minio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.tmp.ImageFileRequest;
import ru.smirnov.musicplatform.precondition.abstraction.domain.AlbumPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.relation.DistributorByArtistPreconditionService;
import ru.smirnov.musicplatform.repository.domain.AlbumRepository;
import ru.smirnov.musicplatform.service.abstraction.minio.AlbumFileManagementService;
import ru.smirnov.musicplatform.service.abstraction.minio.MinioService;

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

    }
}
