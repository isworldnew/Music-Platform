package ru.smirnov.musicplatform.service.abstraction.relation;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;

public interface SavedAlbumService {

    Long addAlbum(Long albumId, DataForToken tokenData);

    void removeAlbum(Long albumId, DataForToken tokenData);
}
