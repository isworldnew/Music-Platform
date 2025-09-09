package ru.smirnov.musicplatform.finder.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistExtendedResponse;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistResponse;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistShortcutResponse;
import ru.smirnov.musicplatform.entity.auxiliary.enums.Role;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.finder.abstraction.ArtistFinderService;
import ru.smirnov.musicplatform.mapper.abstraction.ArtistMapper;
import ru.smirnov.musicplatform.precondition.abstraction.domain.ArtistPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.relation.DistributorByArtistPreconditionService;
import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;
import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;
import ru.smirnov.musicplatform.repository.domain.finder.AlbumFinderRepository;
import ru.smirnov.musicplatform.repository.domain.finder.ArtistFinderRepository;
import ru.smirnov.musicplatform.repository.domain.finder.TrackFinderRepository;

import java.util.List;

@Service
public class ArtistFinderServiceImplementation implements ArtistFinderService {

    private final ArtistPreconditionService artistPreconditionService;
    private final ArtistFinderRepository artistFinderRepository;
    private final AlbumFinderRepository albumFinderRepository;
    private final TrackFinderRepository trackFinderRepository;
    private final ArtistMapper artistMapper;
    private final DistributorByArtistPreconditionService distributorByArtistPreconditionService;

    @Autowired
    public ArtistFinderServiceImplementation(
            ArtistPreconditionService artistPreconditionService,
            ArtistFinderRepository artistFinderRepository,
            AlbumFinderRepository albumFinderRepository,
            TrackFinderRepository trackFinderRepository,
            ArtistMapper artistMapper,
            DistributorByArtistPreconditionService distributorByArtistPreconditionService
    ) {
        this.artistPreconditionService = artistPreconditionService;
        this.artistFinderRepository = artistFinderRepository;
        this.albumFinderRepository = albumFinderRepository;
        this.trackFinderRepository = trackFinderRepository;
        this.artistMapper = artistMapper;
        this.distributorByArtistPreconditionService = distributorByArtistPreconditionService;
    }

    @Override
    public List<ArtistShortcutResponse> searchArtists(String searchRequest) {
        List<Artist> artists = this.artistFinderRepository.searchArtists(searchRequest);
        return artists.stream().map(artist -> this.artistMapper.artistEntityToArtistShortcutResponse(artist)).toList();
    }

    @Override
    public List<ArtistShortcutResponse> searchArtists(String searchRequest, Long distributorId) {
        List<Artist> artists = this.artistFinderRepository.searchArtists(searchRequest, distributorId);
        return artists.stream().map(artist -> this.artistMapper.artistEntityToArtistShortcutResponse(artist)).toList();
    }

    @Override
    public List<ArtistShortcutResponse> getDistributedArtists(Long distributorId, boolean activelyDistributed) {
        List<Artist> artists = this.artistFinderRepository.getDistributedArtists(distributorId, activelyDistributed);
        return artists.stream().map(artist -> this.artistMapper.artistEntityToArtistShortcutResponse(artist)).toList();
    }

    @Override
    public ArtistResponse getArtistData(Long artistId, DataForToken tokenData) {
        // для USER и GUEST: только информация о public-альбомах/треках
        Artist artist = this.artistPreconditionService.getByIdIfExists(artistId);

        List<TrackShortcutProjection> tracks = this.trackFinderRepository.getTracksByArtist(artistId, true);
        List<MusicCollectionShortcutProjection> albums = this.albumFinderRepository.getAlbumsByArtist(artistId, true);

        return this.artistMapper.artistEntityToArtistResponse(artist, albums, tracks);
    }

    @Override
    public ArtistExtendedResponse getArtistExtendedData(Long artistId, DataForToken tokenData) {
        // для ADMIN и DISTRIBUTOR: с историей дистрибьюции
        // ADMIN может смотерть всё вне зависимости от уровня доступа
        // DISTRIBUTOR - тоже, но только если это его ACTIVE-исполнитель
        Artist artist = this.artistPreconditionService.getByIdIfExists(artistId);

        if (tokenData.getRole().equals(Role.DISTRIBUTOR.name())) {
            this.distributorByArtistPreconditionService.checkActiveRelationBetweenDistributorAndArtistExistence(tokenData.getEntityId(), artistId);
        }

        List<TrackShortcutProjection> tracks = this.trackFinderRepository.getTracksByArtist(artistId, false);
        List<MusicCollectionShortcutProjection> albums = this.albumFinderRepository.getAlbumsByArtist(artistId, false);

        return this.artistMapper.artistEntityToArtistExtendedResponse(artist, albums, tracks);
    }

}
