package ru.smirnov.musicplatform.finder.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.dto.domain.track.TrackShortcutResponse;
import ru.smirnov.musicplatform.entity.domain.Track;
import ru.smirnov.musicplatform.finder.abstraction.TrackFinderService;
import ru.smirnov.musicplatform.mapper.abstraction.TrackMapper;
import ru.smirnov.musicplatform.repository.domain.finder.TrackFinderRepository;

import java.util.List;
import java.util.Map;

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
    public List<TrackShortcutResponse> searchTracks(String searchRequest, Long userId, boolean savedOnly) {

        Map<Track, Boolean> tracks = this.trackFinderRepository.searchTracks(searchRequest, userId, savedOnly);

        List<TrackShortcutResponse> trackShortcuts = tracks.keySet().stream()
                .map(track -> this.trackMapper.trackEntityToTrackShortcutResponse(track, tracks.get(track)))
                .toList();

        return trackShortcuts;
    }
}
