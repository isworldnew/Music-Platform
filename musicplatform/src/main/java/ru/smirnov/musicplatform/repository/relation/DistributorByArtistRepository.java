package ru.smirnov.musicplatform.repository.relation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.relation.DistributorsByArtists;

import java.util.List;
import java.util.Optional;


@Repository
public interface DistributorByArtistRepository extends JpaRepository<DistributorsByArtists, Long> {

    @Query(
            value = """
                    INSERT INTO distributors_by_artists(distributor_id, artist_id, status)
                    VALUES (:distributorId, :artistId, :status)
                    RETURNING id
                    """,
            nativeQuery = true
    )
    Long save(@Param("distributorId") Long distributorId, @Param("artistId") Long artistId, @Param("status") String status);

    @Query(
            value = """
                    SELECT
                    	COUNT(*)
                    FROM distributors_by_artists
                    WHERE
                    	distributors_by_artists.distributor_id = :distributorId
                    AND
                    	distributors_by_artists.artist_id = :artistId
                    AND
                    	distributors_by_artists.status = 'ACTIVE'
                    """,
            nativeQuery = true
    )
    Long findActiveRelationBetweenDistributorAndArtist(@Param("distributorId") Long distributorId, @Param("artistId") Long artistId);

    @Query(
            value = """
                    SELECT * FROM distributors_by_artists
                    WHERE distributors_by_artists.distributor_id = :distributorId AND distributors_by_artists.artist_id = :artistId
                    """,
            nativeQuery = true
    )
    Optional<DistributorsByArtists> findByDistributorAndArtist(@Param("distributorId") Long distributorId, @Param("artistId") Long artistId);

    @Query(
            value = """
                    SELECT * FROM distributors_by_artists
                    WHERE distributors_by_artists.artist_id = :artistId
                    """,
            nativeQuery = true
    )
    List<DistributorsByArtists> findRelationsOfArtist(@Param("artistId") Long artistId);
}
