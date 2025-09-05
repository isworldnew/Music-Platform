package ru.smirnov.musicplatform.finder.abstraction;


import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;

import java.util.List;
import java.util.Set;

public interface TrackFinderService {

    List<TrackShortcutProjection> searchTracks(String searchRequest, Long userId, boolean savedOnly);

    List<TrackShortcutProjection> searchTracksByTagsCombination(Set<Long> tagsId);

}
