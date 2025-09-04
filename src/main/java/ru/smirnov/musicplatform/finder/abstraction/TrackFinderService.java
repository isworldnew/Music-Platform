package ru.smirnov.musicplatform.finder.abstraction;


import ru.smirnov.musicplatform.dto.domain.track.TrackShortcutResponse;
import ru.smirnov.musicplatform.projection.TrackShortcutProjection;

import java.util.List;

public interface TrackFinderService {

    List<TrackShortcutProjection> searchTracks(String searchRequest, Long userId, boolean savedOnly);

}
