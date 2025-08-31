package ru.smirnov.musicplatform.service.implementation.minio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.tmp.ImageFileRequest;
import ru.smirnov.musicplatform.precondition.abstraction.domain.PlaylistPreconditionService;
import ru.smirnov.musicplatform.repository.domain.PlaylistRepository;
import ru.smirnov.musicplatform.service.abstraction.minio.MinioService;
import ru.smirnov.musicplatform.service.abstraction.minio.PlaylistFileManagementService;

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

    }

}
