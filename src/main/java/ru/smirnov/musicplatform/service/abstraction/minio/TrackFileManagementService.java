package ru.smirnov.musicplatform.service.abstraction.minio;

import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.tmp.AudioFileRequest;
import ru.smirnov.musicplatform.dto.tmp.ImageFileRequest;

public interface TrackFileManagementService {

    void updateTrackCover(Long trackId, ImageFileRequest dto, DataForToken tokenData);

    void updateTrackAudio(Long trackId, AudioFileRequest dto, DataForToken tokenData);
}
