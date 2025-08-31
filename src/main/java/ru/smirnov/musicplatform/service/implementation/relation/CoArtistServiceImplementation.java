package ru.smirnov.musicplatform.service.implementation.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.tmp.CoArtistRequest;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.entity.domain.Track;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.precondition.abstraction.domain.ArtistPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.domain.TrackPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.relation.DistributorByArtistPreconditionService;
import ru.smirnov.musicplatform.repository.relation.CoArtistRepository;
import ru.smirnov.musicplatform.service.abstraction.relation.CoArtistService;

// [v] checked
@Service
public class CoArtistServiceImplementation implements CoArtistService {

    private final CoArtistRepository coArtistRepository;

    private final ArtistPreconditionService artistPreconditionService;
    private final TrackPreconditionService trackPreconditionService;
    private final DistributorByArtistPreconditionService distributorByArtistPreconditionService;

    @Autowired
    public CoArtistServiceImplementation(
            CoArtistRepository coArtistRepository,
            ArtistPreconditionService artistPreconditionService,
            TrackPreconditionService trackPreconditionService,
            DistributorByArtistPreconditionService distributorByArtistPreconditionService
    ) {
        this.coArtistRepository = coArtistRepository;
        this.artistPreconditionService = artistPreconditionService;
        this.trackPreconditionService = trackPreconditionService;
        this.distributorByArtistPreconditionService = distributorByArtistPreconditionService;
    }

    @Override
    @Transactional
    public Long addCoArtistToTrack(Long trackId, CoArtistRequest dto, DataForToken tokenData) {
        Track track = this.trackPreconditionService.getByIdIfExists(trackId);
        this.distributorByArtistPreconditionService.checkActiveRelationBetweenDistributorAndArtistExistence(tokenData.getEntityId(), track.getArtist().getId());
        Artist artist = this.artistPreconditionService.getByIdIfExists(dto.getCoArtistId());

        if (track.getArtist().getId().equals(dto.getCoArtistId()))
            throw new ConflictException("Adding track's (id=" + trackId + ") artist (id=" + track.getArtist().getId() + ") to track's co-artists is not allowed");

        try {
            return this.coArtistRepository.save(trackId, dto.getCoArtistId());
        }
        catch (DataIntegrityViolationException e) {
            throw new ConflictException("Track (id=" + trackId + ") already has artist (id=" + dto.getCoArtistId() + ") as co-artist");
        }
    }

    @Override
    @Transactional
    public void removeCoArtistFromTrack(Long trackId, CoArtistRequest dto, DataForToken tokenData) {
        Track track = this.trackPreconditionService.getByIdIfExists(trackId);
        this.distributorByArtistPreconditionService.checkActiveRelationBetweenDistributorAndArtistExistence(tokenData.getEntityId(), track.getArtist().getId());
        Artist artist = this.artistPreconditionService.getByIdIfExists(dto.getCoArtistId());
        this.coArtistRepository.delete(trackId, dto.getCoArtistId());
    }
}
