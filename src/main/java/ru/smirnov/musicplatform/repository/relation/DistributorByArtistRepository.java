package ru.smirnov.musicplatform.repository.relation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.relation.DistributorsByArtists;


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

    /*@Query(
            value = """
                    INSERT INTO distributors_by_artists (distributor_id, artist_id, status)
                    VALUES (:distributorId, :artistId, :status)
                    RETURNING id
                    """,
            nativeQuery = true
    )
    Long save(@Param("distributorId") Long distributorId, @Param("artistId") Long artistId, @Param("status") String status);

    // стоит именно по исполнителю искать дистрибьютора:
    // потому что у одного исполнителя может быть только одна ACTIVE-ная связь с одним дистрибьютором
    // это ограничение даёт возможность и по дистрибьютору искать исполнителя, но так всё же надёжнее
    @Query(
            value = """
                    SELECT distributor_id FROM distributors_by_artists WHERE artist_id = :artistId AND status = 'ACTIVE';
                    """,
            nativeQuery = true
    )
    Optional<Long> activeDistributionStatusWithArtist(@Param("artistId") Long artistId);

    @Query(
            value = """
                    SELECT COUNT(*) FROM distributors_by_artists WHERE artist_id = :artistId AND status = 'ACTIVE';
                    """,
            nativeQuery = true
    )
    Long countAmountOfActiveDistributionStatusesByArtistId(@Param("artistId") Long artistId);

    @Query(
            value = """
                    SELECT COUNT(*) FROM distributors_by_artists
                    WHERE distributors_by_artists.distributor_id = :distributorId
                    AND
                    distributors_by_artists.artist_id = :artistId
                    """,
            nativeQuery = true
    )
    Long relationExistenceBetweenDistributorAndArtist(@Param("artistId") Long artistId, @Param("distributorId") Long distributorId);


    @Query(
            value = """
                    SELECT
                        distributors_by_artists.id AS id,
                        distributors_by_artists.distributor_id AS distributor_id,
                        distributors_by_artists.status AS status,
                        distributors.name AS distributor_name
                    FROM distributors_by_artists
                    JOIN distributors
                    ON distributors_by_artists.distributor_id = distributors.id
                    WHERE distributors_by_artists.artist_id = :artistId
                    """,
            nativeQuery = true
    )
    List<DistributorByArtistProjection> findDistributorByArtistProjectionByArtistId(@Param("artistId") Long artistId);


    @Query(
            value = """
                    SELECT
                        COUNT(*)
                    FROM distributors_by_artists
                    WHERE
                    distributors_by_artists.artist_id = :artistId
                    AND
                    distributors_by_artists.status = 'ACTIVE'
                    """,
            nativeQuery = true
    )
    Optional<Integer> countAmountOfActiveRelations(@Param("artistId") Long artistId);*/


}
