package ru.smirnov.musicplatform.finder.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.finder.abstraction.TrackFinderService;
import ru.smirnov.musicplatform.mapper.abstraction.TrackMapper;
import ru.smirnov.musicplatform.precondition.abstraction.domain.TagPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.domain.TrackPreconditionService;
import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;
import ru.smirnov.musicplatform.projection.implementation.TrackShortcutProjectionImplementation;
import ru.smirnov.musicplatform.repository.domain.finder.TrackFinderRepository;

import java.util.List;
import java.util.Set;

@Service
public class TrackFinderServiceImplementation implements TrackFinderService {

    private final TrackFinderRepository trackFinderRepository;
    private final TrackMapper trackMapper;
    private final TagPreconditionService tagPreconditionService;

    @Autowired
    public TrackFinderServiceImplementation(TrackFinderRepository trackFinderRepository, TrackMapper trackMapper, TagPreconditionService tagPreconditionService) {
        this.trackFinderRepository = trackFinderRepository;
        this.trackMapper = trackMapper;
        this.tagPreconditionService = tagPreconditionService;
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
}
