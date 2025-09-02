package ru.smirnov.musicplatform.finder.abstraction;


import ru.smirnov.musicplatform.dto.domain.track.TrackShortcutResponse;

import java.util.List;

public interface TrackFinderService {

    List<TrackShortcutResponse> searchTracks(String searchRequest, Long userId, boolean savedOnly);

}
