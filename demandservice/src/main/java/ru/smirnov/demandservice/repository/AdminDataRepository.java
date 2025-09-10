package ru.smirnov.demandservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.smirnov.demandservice.entity.domain.AdminData;
import ru.smirnov.demandservice.projection.abstraction.AdminClaimProjection;

import java.util.List;

@Repository
public interface AdminDataRepository extends JpaRepository<AdminData, Long> {

    @Query(
            value = """
                    SELECT
                        admin_id AS id,
                        SUM(amount_of_claims) AS amount_of_claims
                    FROM (
                        SELECT
                            distributor_registration_claims.admin_id AS admin_id,
                            COUNT(*) AS amount_of_claims
                        FROM distributor_registration_claims
                        WHERE distributor_registration_claims.status IN ('RECEIVED', 'IN_PROGRESS')
                        GROUP BY distributor_registration_claims.admin_id
                    
                        UNION ALL
                    
                        SELECT
                            track_claims.admin_id AS admin_id,
                            COUNT(*) AS amount_of_claims
                        FROM track_claims
                        WHERE track_claims.status IN ('RECEIVED', 'IN_PROGRESS')
                        GROUP BY track_claims.admin_id
                    ) AS merged_stats
                    GROUP BY admin_id;             
                    """,
            nativeQuery = true
    )
    List<AdminClaimProjection> getClaimPerAdminStats();

}
