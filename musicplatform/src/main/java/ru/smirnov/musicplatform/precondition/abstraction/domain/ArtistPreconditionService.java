package ru.smirnov.musicplatform.precondition.abstraction.domain;

import ru.smirnov.musicplatform.entity.domain.Artist;

public interface ArtistPreconditionService {

    void existsByName(String name);

    Artist getByIdIfExists(Long artistId);

    Artist getByIdIfExistsAndNameIsUnique(Long artistId, String name);
}
