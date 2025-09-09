package ru.smirnov.musicplatform.finder.abstraction;


import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.track.TrackExtendedResponse;
import ru.smirnov.musicplatform.dto.domain.track.TrackResponse;
import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;

import java.util.List;
import java.util.Set;

public interface TrackFinderService {

    List<TrackShortcutProjection> searchTracks(String searchRequest, Long userId, boolean savedOnly);

    List<TrackShortcutProjection> searchTracksByTagsCombination(Long userId, Set<Long> tagsId);

    List<TrackShortcutProjection> getSavedTracks(Long userId);

    TrackResponse getTrackData(Long trackId, DataForToken tokenData);

    TrackExtendedResponse getTrackExtendedData(Long trackId, DataForToken tokenData);

    List<TrackShortcutProjection> searchTracksGloballyAdmin(String searchRequest);
}
