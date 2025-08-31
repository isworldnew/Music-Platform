package ru.smirnov.musicplatform.service.abstraction.minio;

import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.tmp.ImageFileRequest;

public interface PlaylistFileManagementService {

    void updatePlaylistCover(Long playlistId, ImageFileRequest dto, DataForToken tokenData);

}
