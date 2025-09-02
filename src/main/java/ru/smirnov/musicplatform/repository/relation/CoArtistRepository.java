package ru.smirnov.musicplatform.repository.relation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.relation.CoArtists;


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

}
