package ru.smirnov.musicplatform.precondition.abstraction.domain;

import ru.smirnov.musicplatform.entity.domain.Playlist;

public interface PlaylistPreconditionService {

    void existsByNameAndUserId(String name, Long userId);

    Playlist getByIdIfExists(Long playlistId);

    Playlist existsAndBelongToUser(Long playlistId, Long userId);

    Playlist getByIdIfExistsAndNameIsUnique(Long playlistId, Long userId, String name);
}
