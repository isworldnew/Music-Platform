package ru.smirnov.musicplatform.finder.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.tag.TagResponse;
import ru.smirnov.musicplatform.dto.domain.track.TrackExtendedResponse;
import ru.smirnov.musicplatform.dto.domain.track.TrackResponse;
import ru.smirnov.musicplatform.entity.auxiliary.enums.Role;
import ru.smirnov.musicplatform.entity.domain.Track;
import ru.smirnov.musicplatform.entity.relation.SavedTracks;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.finder.abstraction.TrackFinderService;
import ru.smirnov.musicplatform.mapper.abstraction.TagMapper;
import ru.smirnov.musicplatform.mapper.abstraction.TrackMapper;
import ru.smirnov.musicplatform.precondition.abstraction.domain.TagPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.domain.TrackPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.relation.DistributorByArtistPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.relation.SavedTrackPreconditionService;
import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;
import ru.smirnov.musicplatform.projection.implementation.TrackShortcutProjectionImplementation;
import ru.smirnov.musicplatform.repository.domain.finder.TrackFinderRepository;
import ru.smirnov.musicplatform.repository.relation.SavedTrackRepository;
import ru.smirnov.musicplatform.service.abstraction.domain.TagService;

import java.util.List;
import java.util.Set;

@Service
public class TrackFinderServiceImplementation implements TrackFinderService {

    private final TrackFinderRepository trackFinderRepository;
    private final TrackMapper trackMapper;
    private final TrackPreconditionService trackPreconditionService;
    private final TagPreconditionService tagPreconditionService;
    private final DistributorByArtistPreconditionService distributorByArtistPreconditionService;
    private final TagMapper tagMapper;
    private final SavedTrackRepository savedTrackRepository;

    @Autowired
    public TrackFinderServiceImplementation(
            TrackFinderRepository trackFinderRepository,
            TrackMapper trackMapper,
            TrackPreconditionService trackPreconditionService,
            TagPreconditionService tagPreconditionService,
            DistributorByArtistPreconditionService distributorByArtistPreconditionService,
            TagMapper tagMapper,
            SavedTrackRepository savedTrackRepository
    ) {
        this.trackFinderRepository = trackFinderRepository;
        this.trackMapper = trackMapper;
        this.trackPreconditionService = trackPreconditionService;
        this.tagPreconditionService = tagPreconditionService;
        this.distributorByArtistPreconditionService = distributorByArtistPreconditionService;
        this.tagMapper = tagMapper;
        this.savedTrackRepository = savedTrackRepository;
    }

    @Override
    public List<TrackShortcutProjection> searchTracks(String searchRequest, Long userId, boolean savedOnly) {

        List<TrackShortcutProjection> tracks = this.trackFinderRepository.searchTracks(searchRequest, userId, savedOnly);

        for (TrackShortcutProjection track : tracks) {
            if (!track.getStatus().isAvailable()) {
                ((TrackShortcutProjectionImplementation) track).setImageReference(null);
            }
        }

        return tracks;
    }

    @Override
    public List<TrackShortcutProjection> searchTracksByTagsCombination(Long userId, Set<Long> tagsId) {

        tagsId.forEach(tagId -> this.tagPreconditionService.getByIdIfExistsAndBelongsToUser(tagId, userId));

        List<TrackShortcutProjection> tracks = this.trackFinderRepository.searchTracksByTagsCombination(tagsId);

        for (TrackShortcutProjection track : tracks) {
            if (!track.getStatus().isAvailable()) {
                ((TrackShortcutProjectionImplementation) track).setImageReference(null);
            }
        }

        return tracks;
    }

    @Override
    public List<TrackShortcutProjection> getSavedTracks(Long userId) {

        List<TrackShortcutProjection> tracks = this.trackFinderRepository.getSavedTracks(userId);

        for (TrackShortcutProjection track : tracks) {
            if (!track.getStatus().isAvailable()) {
                ((TrackShortcutProjectionImplementation) track).setImageReference(null);
            }
        }

        return tracks;
    }

    @Override
    public TrackResponse getTrackData(Long trackId, DataForToken tokenData) {
        // для ANONYMOUS, ADMIN и DISTRIBUTOR
        Track track = this.trackPreconditionService.getByIdIfExists(trackId);

        if (
                tokenData.getAuthorities().stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList()
                .contains("ROLE_ANONYMOUS")
        ) {
            if (!track.getStatus().isAvailable())
                throw new ForbiddenException("Track (id=" + trackId + ") is not PUBLIC");
        }

        else if (tokenData.getRole().equals(Role.DISTRIBUTOR.name())) {
            this.distributorByArtistPreconditionService.checkActiveRelationBetweenDistributorAndArtistExistence(tokenData.getEntityId(), track.getArtist().getId());
        }

        return this.trackMapper.trackEntityToTrackResponse(track);
    }

    @Override
    public TrackExtendedResponse getTrackExtendedData(Long trackId, DataForToken tokenData) {
        // для USER
        Track track = this.trackPreconditionService.getIfExistsAndPublic(trackId);
        List<TagResponse> tags = track.getTags().stream().map(tag -> this.tagMapper.tagEntityToTagResponse(tag.getTag())).toList();

        SavedTracks savedTrack = this.savedTrackRepository.findByTrackIdAndUserId(trackId, tokenData.getEntityId()).orElse(null);

        return this.trackMapper.trackEntityToTrackExtendedResponse(track, tags, savedTrack != null);
    }

    @Override
    public List<TrackShortcutProjection> searchTracksGloballyAdmin(String searchRequest) {
        return this.trackFinderRepository.searchTracksGloballyAdmin(searchRequest);
    }

}
