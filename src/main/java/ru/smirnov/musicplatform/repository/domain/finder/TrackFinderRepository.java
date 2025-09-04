package ru.smirnov.musicplatform.repository.domain.finder;

import ru.smirnov.musicplatform.entity.domain.Track;
import ru.smirnov.musicplatform.projection.TrackShortcutProjection;

import java.util.List;
import java.util.Map;

public interface TrackFinderRepository {

    List<TrackShortcutProjection> searchTracks(String searchRequest, Long userId, boolean savedOnly);
}
