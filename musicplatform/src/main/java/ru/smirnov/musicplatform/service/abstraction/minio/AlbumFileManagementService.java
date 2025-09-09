package ru.smirnov.musicplatform.service.abstraction.minio;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.file.ImageFileRequest;

public interface AlbumFileManagementService {

    void updateAlbumCover(Long albumId, ImageFileRequest dto, DataForToken tokenData);
}
