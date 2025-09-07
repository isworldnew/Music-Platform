package ru.smirnov.musicplatform.finder.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionResponse;
import ru.smirnov.musicplatform.entity.auxiliary.enums.Role;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.finder.abstraction.AlbumFinderService;
import ru.smirnov.musicplatform.mapper.abstraction.AlbumMapper;
import ru.smirnov.musicplatform.precondition.abstraction.domain.AlbumPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.relation.DistributorByArtistPreconditionService;
import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;
import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;
import ru.smirnov.musicplatform.projection.implementation.MusicCollectionShortcutProjectionImplementation;
import ru.smirnov.musicplatform.repository.domain.AlbumRepository;
import ru.smirnov.musicplatform.repository.domain.finder.AlbumFinderRepository;
import ru.smirnov.musicplatform.repository.domain.finder.TrackFinderRepository;

import java.util.List;

@Service
public class AlbumFinderServiceImplementation implements AlbumFinderService {

    private final AlbumFinderRepository albumFinderRepository;
    private final AlbumPreconditionService albumPreconditionService;
    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;
    private final TrackFinderRepository trackFinderRepository;
    private final DistributorByArtistPreconditionService distributorByArtistPreconditionService;

    @Autowired
    public AlbumFinderServiceImplementation(
            AlbumFinderRepository albumFinderRepository,
            AlbumPreconditionService albumPreconditionService,
            AlbumRepository albumRepository,
            AlbumMapper albumMapper, TrackFinderRepository trackFinderRepository,
            DistributorByArtistPreconditionService distributorByArtistPreconditionService
    ) {
        this.albumFinderRepository = albumFinderRepository;
        this.albumPreconditionService = albumPreconditionService;
        this.albumRepository = albumRepository;
        this.albumMapper = albumMapper;
        this.trackFinderRepository = trackFinderRepository;
        this.distributorByArtistPreconditionService = distributorByArtistPreconditionService;
    }

    @Override
    public List<MusicCollectionShortcutProjection> searchAlbums(String searchRequest, Long userId, boolean savedOnly) {

        List<MusicCollectionShortcutProjection> albums = this.albumFinderRepository.searchAlbums(searchRequest, userId, savedOnly);

        for (MusicCollectionShortcutProjection album : albums) {
            if (!album.getAccessLevel().isAvailable()) {
                ((MusicCollectionShortcutProjectionImplementation) album).setImageReference(null);
            }
        }

        return albums;
    }

    @Override
    public List<MusicCollectionShortcutProjection> getSavedAlbums(Long userId) {

        List<MusicCollectionShortcutProjection> albums = this.albumFinderRepository.getSavedAlbums(userId);

        for (MusicCollectionShortcutProjection album : albums) {
            if (!album.getAccessLevel().isAvailable()) {
                ((MusicCollectionShortcutProjectionImplementation) album).setImageReference(null);
            }
        }

        return albums;
    }

    @Override
    @Transactional
    public MusicCollectionResponse getAlbumById(Long albumId, DataForToken tokenData) {
        // для GUEST, USER и DISTRIBUTOR
        Album album = this.albumPreconditionService.getByIdIfExists(albumId);
        List<TrackShortcutProjection> tracks;

        if (!tokenData.getRole().equals(Role.DISTRIBUTOR.name())) {

            if (!album.getAccessLevel().isAvailable())
                throw new ForbiddenException("Album (id=" + albumId + ") is not PUBLIC");

            album.setNumberOfPlays(album.getNumberOfPlays() + 1);
            this.albumRepository.save(album);

            tracks = this.trackFinderRepository.getTracksByAlbum(albumId, true);
            return this.albumMapper.albumEntityToMusicCollectionResponse(album, tracks);
        }

        else
            this.distributorByArtistPreconditionService.checkActiveRelationBetweenDistributorAndArtistExistence(tokenData.getEntityId(), album.getArtist().getId());

        tracks = this.trackFinderRepository.getTracksByAlbum(albumId, false);
        return this.albumMapper.albumEntityToMusicCollectionResponse(album, tracks);
    }
}
