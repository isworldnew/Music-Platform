package ru.smirnov.musicplatform.service.abstraction.domain;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.track.TrackAccessLevelRequest;
import ru.smirnov.musicplatform.dto.domain.track.TrackRequest;

public interface TrackService {

    Long uploadTrack(Long artistId, TrackRequest dto, DataForToken tokenData);

    void updateTrack(Long trackId, TrackRequest dto, DataForToken tokenData);

    void updateTrackAccessLevel(Long trackId, TrackAccessLevelRequest dto, DataForToken tokenData);

    void listenToTrack(Long trackId);

    /*
    TrackResponse getTrackByIdWithNoRestrictions(Long trackId, DataForToken tokenData);

    TrackResponse getTrackById(Long trackId);

    ExtendedTrackResponse getTrackWithPossibleRestrictions(Long trackId, DataForToken tokenData);
    */
}
