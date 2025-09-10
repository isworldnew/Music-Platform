package ru.smirnov.demandservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.smirnov.demandservice.entity.domain.TrackClaim;

import java.util.List;

@Repository
public interface TrackClaimRepository extends JpaRepository<TrackClaim, Long> {
    
    @Query(
            value = """
                    SELECT * FROM track_claims
                    WHERE track_claims.admin_id = :adminId
                    AND track_claims.status IN ('RECEIVED', 'IN_PROGRESS')
                    ORDER BY track_claims.creation_date DESC;
                    """,
            nativeQuery = true
    )
    List<TrackClaim> findAllRelevantByAdminId(@Param("adminId") Long adminId);

    @Query(
            value = """
                    SELECT * FROM track_claims
                    WHERE track_claims.admin_id = :adminId
                    AND track_claims.status IN ('COMPLETED', 'DENIED')
                    ORDER BY track_claims.creation_date DESC;
                    """,
            nativeQuery = true
    )
    List<TrackClaim> findAllIrrelevantByAdminId(@Param("adminId") Long adminId);
    
}
