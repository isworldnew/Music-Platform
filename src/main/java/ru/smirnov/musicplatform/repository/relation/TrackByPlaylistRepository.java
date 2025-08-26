package ru.smirnov.musicplatform.repository.relation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.domain.Track;
import ru.smirnov.musicplatform.entity.relation.TracksByPlaylists;

@Repository
public interface TrackByPlaylistRepository extends JpaRepository<TracksByPlaylists, Track> {
}
