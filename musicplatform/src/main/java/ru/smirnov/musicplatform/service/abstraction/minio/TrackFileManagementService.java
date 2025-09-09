package ru.smirnov.musicplatform.service.abstraction.minio;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.file.AudioFileRequest;
import ru.smirnov.musicplatform.dto.file.ImageFileRequest;

public interface TrackFileManagementService {

    void updateTrackCover(Long trackId, ImageFileRequest dto, DataForToken tokenData);

    void updateTrackAudio(Long trackId, AudioFileRequest dto, DataForToken tokenData);
}
