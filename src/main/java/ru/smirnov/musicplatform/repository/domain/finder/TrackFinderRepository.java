package ru.smirnov.musicplatform.repository.domain.finder;

import ru.smirnov.musicplatform.entity.domain.Track;

import java.util.Map;

public interface TrackFinderRepository {

    Map<Track, Boolean> searchTracks(String searchRequest, Long userId, boolean savedOnly);
}
