package ru.smirnov.musicplatform.repository.domain.finder;

import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.entity.domain.Album;

import java.util.Map;

public interface AlbumFinderRepository {

    Map<Album, Boolean> searchAlbums(String searchRequest, User user, boolean savedOnly);
}
