package ru.smirnov.musicplatform.repository.relation;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.relation.TracksByAlbums;

@Repository
public interface TrackByAlbumRepository extends JpaRepository<TracksByAlbums, Long> {

    @Query(
            value = """
                    INSERT INTO tracks_by_albums(album_id, track_id)
                    VALUES (:albumId, :trackId)
                    RETURNING id
                    """,
            nativeQuery = true
    )
    Long save(@Param("albumId") Long albumId, @Param("trackId") Long trackId) throws DataIntegrityViolationException;

    @Query(
            value = """
                    DELETE FROM tracks_by_albums
                    WHERE album_id = :albumId AND track_id = :trackId
                    """,
            nativeQuery = true
    )
    @Modifying
    void delete(@Param("albumId") Long albumId, @Param("trackId") Long trackId);
}
