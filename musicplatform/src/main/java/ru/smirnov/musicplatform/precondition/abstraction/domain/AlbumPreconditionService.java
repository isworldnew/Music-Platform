package ru.smirnov.musicplatform.precondition.abstraction.domain;

import ru.smirnov.musicplatform.entity.domain.Album;

public interface AlbumPreconditionService {

    void existsByNameAndArtistId(String name, Long artistId);

    Album getByIdIfExists(Long albumId);

    Album getByIdIfExistsAndNameIsUnique(Long albumId, String name);
}
