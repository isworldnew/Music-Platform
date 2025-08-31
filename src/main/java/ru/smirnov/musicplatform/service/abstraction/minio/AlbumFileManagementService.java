package ru.smirnov.musicplatform.service.abstraction.minio;

import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.tmp.ImageFileRequest;

public interface AlbumFileManagementService {

    void updateAlbumCover(Long albumId, ImageFileRequest dto, DataForToken tokenData);
}
