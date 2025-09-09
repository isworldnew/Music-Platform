package ru.smirnov.musicplatform.service.abstraction.relation;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;

public interface SavedTrackService {

    Long saveTrack(Long trackId, DataForToken tokenData);

    void deleteTrackFromSaved(Long trackId, DataForToken tokenData);
}
