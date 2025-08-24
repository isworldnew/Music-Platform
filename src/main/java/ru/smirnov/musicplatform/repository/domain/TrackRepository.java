package ru.smirnov.musicplatform.repository.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.domain.Track;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
}
