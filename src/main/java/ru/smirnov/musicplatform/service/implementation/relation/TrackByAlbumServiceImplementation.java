package ru.smirnov.musicplatform.service.implementation.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.tmp.MusicCollectionTracksUpdateRequest;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.precondition.abstraction.domain.AlbumPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.domain.TrackPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.relation.DistributorByArtistPreconditionService;
import ru.smirnov.musicplatform.repository.relation.TrackByAlbumRepository;
import ru.smirnov.musicplatform.service.abstraction.relation.TrackByAlbumService;
import ru.smirnov.musicplatform.util.SetUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public List<Long> updateContent(Long albumId, MusicCollectionTracksUpdateRequest dto, DataForToken tokenData) {
        /*
        Проверки:
        [v] Данный альбом существует
        [v] Дистрибьютор имеет право им управлять
        [v] Все треки существуют, притом исполнитель в них либо автор, либо соавтор
        */
        Album album = this.albumPreconditionService.getByIdIfExists(albumId);
        this.distributorByArtistPreconditionService.checkActiveRelationBetweenDistributorAndArtistExistence(tokenData.getEntityId(), album.getArtist().getId());

        Set<Long> newTrackSet = dto.getNewTrackSet().stream()
                .map(trackId -> this.trackPreconditionService.getIfOwnedOrCollaboratedByArtist(trackId, album.getArtist().getId()))
                .map(track -> track.getId())
                .collect(Collectors.toSet());

        Set<Long> oldTrackSet = album.getTracks().stream().map(track -> track.getTrack().getId()).collect(Collectors.toSet());

        Set<Long> tracksToAdd = SetUtil.findAddedValues(oldTrackSet, newTrackSet);
        Set<Long> tracksToRemove = SetUtil.findRemovedValues(oldTrackSet, newTrackSet);

        List<Long> createdRelations = new ArrayList<>();

        for (Long trackToAdd : tracksToAdd) {
            try {
                createdRelations.add(this.trackByAlbumRepository.save(albumId, trackToAdd));
            }
            catch (DataIntegrityViolationException e) {
                throw new ConflictException("Track (id=" + tracksToAdd + ") already exists in album (id=" + albumId + ")");
            }
        }

        for (Long trackToRemove : tracksToRemove) this.trackByAlbumRepository.delete(albumId, trackToRemove);

        return createdRelations;
    }

}
