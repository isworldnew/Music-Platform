package ru.smirnov.musicplatform.repository.domain;

import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.repository.auxiliary.MusicCollectionRepository;

@Repository
public interface AlbumRepository extends MusicCollectionRepository<Album, Long> {
}
