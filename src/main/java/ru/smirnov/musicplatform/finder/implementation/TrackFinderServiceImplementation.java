package ru.smirnov.musicplatform.finder.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.finder.abstraction.TrackFinderService;
import ru.smirnov.musicplatform.mapper.abstraction.TrackMapper;
import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;
import ru.smirnov.musicplatform.projection.implementation.TrackShortcutProjectionImplementation;
import ru.smirnov.musicplatform.repository.domain.finder.TrackFinderRepository;

import java.util.List;

@Service
public class TrackFinderServiceImplementation implements TrackFinderService {

    private final TrackFinderRepository trackFinderRepository;
    private final TrackMapper trackMapper;

    @Autowired
    public TrackFinderServiceImplementation(TrackFinderRepository trackFinderRepository, TrackMapper trackMapper) {
        this.trackFinderRepository = trackFinderRepository;
        this.trackMapper = trackMapper;
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
}
