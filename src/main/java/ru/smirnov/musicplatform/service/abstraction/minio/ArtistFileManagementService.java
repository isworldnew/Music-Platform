package ru.smirnov.musicplatform.service.abstraction.minio;

import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.tmp.ImageFileRequest;

public interface ArtistFileManagementService {

    void updateArtistCover(Long artistId, ImageFileRequest dto, DataForToken tokenData);

}
