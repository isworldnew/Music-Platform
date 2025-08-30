package ru.smirnov.musicplatform.precondition.abstraction.domain;

import ru.smirnov.musicplatform.entity.domain.Track;

public interface TrackPreconditionService {

    void existsByName(Long artistId, String name);

    Track getByIdIfExists(Long trackId);

    Track getByIdIfExistsAndNameIsUniquePerArtist(Long trackId, String name);

    Track getIfOwnedOrCollaboratedByArtist(Long trackId, Long artistId);
}
