package ru.smirnov.demandservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.smirnov.demandservice.entity.domain.DistributorRegistrationClaim;

import java.util.List;

@Repository
public interface DistributorRegistrationClaimRepository extends JpaRepository<DistributorRegistrationClaim, Long> {

    @Query(
            value = """
                    SELECT * FROM distributor_registration_claims
                    WHERE distributor_registration_claims.admin_id = :adminId
                    AND distributor_registration_claims.status IN (:statuses)
                    ORDER BY distributor_registration_claims.creation_date DESC;
                    """,
            nativeQuery = true
    )
    List<DistributorRegistrationClaim> findAllByAdminIdAndRelevance(@Param("adminId") Long adminId, @Param("statuses") List<String> statuses);
}
