package ru.smirnov.musicplatform.repository.relation;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.domain.Track;
import ru.smirnov.musicplatform.entity.relation.TracksByPlaylists;

@Repository
public interface TrackByPlaylistRepository extends JpaRepository<TracksByPlaylists, Track> {

    @Query(
            value = """
                    INSERT INTO tracks_by_playlists(track_id, playlist_id)
                    VALUES(:trackId, :playlistId)
                    RETURNING id
                    """,
            nativeQuery = true
    )
    Long save(@Param("playlistId") Long playlistId, @Param("trackId") Long trackId) throws DataIntegrityViolationException;

    @Query(
            value = """
                    DELETE FROM tracks_by_playlists
                    WHERE tracks_by_playlists.track_id = :trackId AND tracks_by_playlists.playlist_id = :playlistId
                    """,
            nativeQuery = true
    )
    @Modifying
    void delete(@Param("playlistId") Long playlistId, @Param("trackId") Long trackId);
    
}
