package ru.smirnov.musicplatform.service.abstraction.relation;

import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.tmp.MusicCollectionTracksUpdateRequest;

import java.util.List;

public interface TrackByAlbumService {

    List<Long> updateContent(Long albumId, MusicCollectionTracksUpdateRequest dto, DataForToken tokenData);

}
