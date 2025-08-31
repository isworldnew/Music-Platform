package ru.smirnov.musicplatform.repository.relation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.relation.CoArtists;
import ru.smirnov.musicplatform.projection.CoArtistProjection;

import java.util.List;

@Repository
public interface CoArtistRepository extends JpaRepository<CoArtists, Long> {

    @Query(
            value = """
                    INSERT INTO co_artists (artist_id, track_id)
                    VALUES (:artistId, :trackId)
                    RETURNING id
                    """,
            nativeQuery = true
    )
    Long save(@Param("trackId") Long trackId, @Param("artistId") Long artistId);

    @Query(
            value = """
                    DELETE FROM co_artists
                    WHERE co_artists.track_id = :trackId AND co_artists.artist_id = :artistId
                    """,
            nativeQuery = true
    )
    void delete(@Param("trackId") Long trackId, @Param("artistId") Long artistId);

    @Query(
            value = """
                    SELECT
                    	co_artists.id AS id,
                    	co_artists.id AS artist_id,
                    	artists.name AS name
                    FROM co_artists
                    JOIN artists
                    ON co_artists.artist_id = artists.id
                    WHERE co_artists.track_id = :trackId;
                    """,
            nativeQuery = true
    )
    List<CoArtistProjection> getCoArtistProjections(@Param("trackId") Long trackId);

}
