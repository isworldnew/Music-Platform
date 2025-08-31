package ru.smirnov.musicplatform.service.implementation.minio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.tmp.AudioFileRequest;
import ru.smirnov.musicplatform.dto.tmp.ImageFileRequest;
import ru.smirnov.musicplatform.precondition.abstraction.domain.TrackPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.relation.DistributorByArtistPreconditionService;
import ru.smirnov.musicplatform.repository.domain.TrackRepository;
import ru.smirnov.musicplatform.service.abstraction.minio.MinioService;
import ru.smirnov.musicplatform.service.abstraction.minio.TrackFileManagementService;

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

    }

    @Override
    @Transactional
    public void updateTrackAudio(Long trackId, AudioFileRequest dto, DataForToken tokenData) {

    }
}
