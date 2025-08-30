package ru.smirnov.musicplatform.service.abstraction.domain;

import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.tmp.ExtendedTrackResponse;
import ru.smirnov.musicplatform.dto.tmp.TrackAccessLevelRequest;
import ru.smirnov.musicplatform.dto.tmp.TrackRequest;
import ru.smirnov.musicplatform.dto.tmp.TrackResponse;

public interface TrackService {

    Long uploadTrack(Long artistId, TrackRequest dto, DataForToken tokenData);

    void updateTrack(Long trackId, TrackRequest dto, DataForToken tokenData);

    void updateTrackAccessLevel(Long trackId, TrackAccessLevelRequest dto, DataForToken tokenData);

    void listenToTrack(Long trackId);

    TrackResponse getTrackByIdWithNoRestrictions(Long trackId);

    TrackResponse getTrackById(Long trackId);

    ExtendedTrackResponse getTrackWithPossibleRestrictions(Long trackId);

}
