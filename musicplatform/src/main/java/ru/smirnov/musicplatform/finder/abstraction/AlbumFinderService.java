package ru.smirnov.musicplatform.finder.abstraction;

import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionResponse;
import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;

import java.util.List;

public interface AlbumFinderService {

    List<MusicCollectionShortcutProjection> searchAlbums(String searchRequest, Long userId, boolean savedOnly);

    List<MusicCollectionShortcutProjection> getSavedAlbums(Long userId);

    MusicCollectionResponse getAlbumById(Long albumId, DataForToken tokenData);

}
