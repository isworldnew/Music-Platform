package ru.smirnov.musicplatform.service.abstraction.relation;

import ru.smirnov.musicplatform.authentication.DataForToken;

public interface SavedTrackService {

    Long saveTrack(Long trackId, DataForToken tokenData);

    void deleteTrackFromSaved(Long trackId, DataForToken tokenData);
}
