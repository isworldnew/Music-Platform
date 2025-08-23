package ru.smirnov.musicplatform.repository.relation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.auxiliary.enums.DistributionStatus;
import ru.smirnov.musicplatform.entity.relation.DistributorsByArtists;

@Repository
public interface DistributorByArtistRepository extends JpaRepository<DistributorsByArtists, Long> {

    @Query(
            value = """
                    INSERT INTO distributors_by_artists (distributor_id, artist_id, status)
                    VALUES (:distributorId, :artistId, :status)
                    RETURNING id
                    """,
            nativeQuery = true
    )
    Long save(@Param("distributorId") Long distributorId, @Param("artistId") Long artistId, @Param("status") String status);

}
