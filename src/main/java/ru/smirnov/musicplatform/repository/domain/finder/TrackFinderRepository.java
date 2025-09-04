package ru.smirnov.musicplatform.repository.domain.finder;

import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;

import java.util.List;

public interface TrackFinderRepository {

    List<TrackShortcutProjection> searchTracks(String searchRequest, Long userId, boolean savedOnly);
}
