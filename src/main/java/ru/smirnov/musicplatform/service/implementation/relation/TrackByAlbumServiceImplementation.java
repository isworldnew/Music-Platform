package ru.smirnov.musicplatform.service.implementation.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.entity.domain.Track;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.precondition.abstraction.domain.AlbumPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.domain.TrackPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.relation.DistributorByArtistPreconditionService;
import ru.smirnov.musicplatform.repository.relation.TrackByAlbumRepository;
import ru.smirnov.musicplatform.service.abstraction.relation.TrackByAlbumService;


@Service
public class TrackByAlbumServiceImplementation implements TrackByAlbumService {

    private final TrackByAlbumRepository trackByAlbumRepository;

    private final TrackPreconditionService trackPreconditionService;
    private final AlbumPreconditionService albumPreconditionService;
    private final DistributorByArtistPreconditionService distributorByArtistPreconditionService;

    @Autowired
    public TrackByAlbumServiceImplementation(
            TrackByAlbumRepository trackByAlbumRepository,
            TrackPreconditionService trackPreconditionService,
            AlbumPreconditionService albumPreconditionService,
            DistributorByArtistPreconditionService distributorByArtistPreconditionService
    ) {
        this.trackByAlbumRepository = trackByAlbumRepository;
        this.trackPreconditionService = trackPreconditionService;
        this.albumPreconditionService = albumPreconditionService;
        this.distributorByArtistPreconditionService = distributorByArtistPreconditionService;
    }

    @Override
    @Transactional
    public Long addTrack(Long albumId, Long trackId, DataForToken tokenData) {
        /*
        Проверки:
        [v] Данный альбом существует
        [v] Дистрибьютор имеет право им управлять
        [v] Трек существует, притом исполнитель в нём либо автор, либо соавтор
        */
        Album album = this.albumPreconditionService.getByIdIfExists(albumId);
        this.distributorByArtistPreconditionService.checkActiveRelationBetweenDistributorAndArtistExistence(tokenData.getEntityId(), album.getArtist().getId());
        Track track = this.trackPreconditionService.getIfOwnedOrCollaboratedByArtist(trackId, album.getArtist().getId());

        try {
            return this.trackByAlbumRepository.save(albumId, trackId);
        }
        catch (DataIntegrityViolationException e) {
            throw new ConflictException("Track (id=" + trackId + ") already exists in album (id=" + albumId + ")");
        }
    }

    @Override
    @Transactional
    public void removeTrack(Long albumId, Long trackId, DataForToken tokenData) {
        Album album = this.albumPreconditionService.getByIdIfExists(albumId);
        this.distributorByArtistPreconditionService.checkActiveRelationBetweenDistributorAndArtistExistence(tokenData.getEntityId(), album.getArtist().getId());
        Track track = this.trackPreconditionService.getIfOwnedOrCollaboratedByArtist(trackId, album.getArtist().getId());

        this.trackByAlbumRepository.delete(albumId, trackId);
    }

}
