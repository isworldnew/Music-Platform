package ru.smirnov.musicplatform.repository.domain.finder;

import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;

import java.util.List;
import java.util.Set;

public interface TrackFinderRepository {

    List<TrackShortcutProjection> searchTracks(String searchRequest, Long userId, boolean savedOnly);

    List<TrackShortcutProjection> searchTracksByTagsCombination(Set<Long> tagsId);

    List<TrackShortcutProjection> getSavedTracks(Long userId);

    List<TrackShortcutProjection> getTracksByArtist(Long artistId, boolean publicOnly);

    List<TrackShortcutProjection> searchTracksGloballyAdmin(String searchRequest);

    List<TrackShortcutProjection> getTracksByChart(Long chartId, boolean publicOnly);

    List<TrackShortcutProjection> getTracksByAlbum(Long albumId, boolean publicOnly);

    List<TrackShortcutProjection> getTracksByPlaylist(Long playlistId, boolean publicOnly);
}
