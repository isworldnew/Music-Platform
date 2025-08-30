package ru.smirnov.musicplatform.repository.relation;

import org.springframework.dao.DataIntegrityViolationException;
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
                    INSERT INTO saved_tracks(user_id, track_id)
                    VALUES (:userId, :trackId)
                    RETURNING id
                    """,
            nativeQuery = true
    )
    Long save(@Param("userId") Long userId, @Param("trackId") Long trackId) throws DataIntegrityViolationException;

    @Query(
            value = """
                    DELETE FROM saved_tracks
                    WHERE user_id = :userId AND track_id = :trackId
                    """,
            nativeQuery = true
    )
    @Modifying
    void delete(@Param("userId") Long userId, @Param("trackId") Long trackId);

    Optional<SavedTracks> findByTrackIdAndUserId(Long trackId, Long userId);
}
