package ru.smirnov.musicplatform.repository.relation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.relation.SavedTracks;
import ru.smirnov.musicplatform.projection.SavedTrackProjection;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedTrackRepository extends JpaRepository<SavedTracks, Long> {

    @Query(
            value = """
                    SELECT
                        CASE
                            WHEN COUNT(*) > 0 THEN TRUE
                            ELSE FALSE
                        END AS is_saved
                    FROM saved_tracks
                    WHERE saved_tracks.user_id = :userId AND saved_tracks.track_id = :trackId;
                    """,
            nativeQuery = true
    )
    boolean trackHasBeenSavedAlready(@Param("userId") Long userId, @Param("trackId") Long trackId);

    @Query(
            value = """
                    INSERT INTO saved_tracks (user_id, track_id)
                    VALUES (:userId, :trackId)
                    RETURNING id
                    """,
            nativeQuery = true
    )
    Long save(@Param("userId") Long userId, @Param("trackId") Long trackId);

    @Query(
            value = """
                    DELETE FROM saved_tracks
                    WHERE saved_tracks.user_id = :userId AND saved_tracks.track_id = :trackId
                    """,
            nativeQuery = true
    )
    @Modifying
    void deleteByTrackIdAndUserId(@Param("userId") Long userId, @Param("trackId") Long trackId);

    @Query(
            value = """
                    SELECT
                        saved_tracks.id AS id,
                        tracks.id AS track_id,
                        tracks.name AS track_name,
                        artists.id AS artist_id,
                        artists.name AS artist_name
                    FROM saved_tracks
                    JOIN tracks
                    ON saved_tracks.track_id = tracks.id
                    JOIN artists
                    ON tracks.artist_id = artists.id
                    WHERE saved_tracks.user_id = :userId
                    """,
            nativeQuery = true
    )
    List<SavedTrackProjection> savedTracksByUserId(@Param("userId") Long userId);

}
