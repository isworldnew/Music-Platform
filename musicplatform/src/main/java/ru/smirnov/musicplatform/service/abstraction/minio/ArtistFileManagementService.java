package ru.smirnov.musicplatform.service.abstraction.minio;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.file.ImageFileRequest;

public interface ArtistFileManagementService {

    void updateArtistCover(Long artistId, ImageFileRequest dto, DataForToken tokenData);

}
