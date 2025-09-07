package ru.smirnov.musicplatform.repository.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.repository.domain.finder.ArtistFinderRepository;

import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    Optional<Artist> findByName(String name);

}
