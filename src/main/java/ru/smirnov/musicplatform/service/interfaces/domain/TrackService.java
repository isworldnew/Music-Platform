package ru.smirnov.musicplatform.service.interfaces.domain;

import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.track.TrackAccessLevelUpdateDto;

public interface TrackService {

    Long uploadTrack(TrackRequestDto dto, DataForToken tokenData);

    Long updateTrack(Long trackId, TrackRequestDto dto, DataForToken tokenData);

    Void listenToTrack(Long trackId);

    ? updateTrackAccessLevel(Long trackId, TrackAccessLevelRequestDto dto, DataForToken tokenData);

}
