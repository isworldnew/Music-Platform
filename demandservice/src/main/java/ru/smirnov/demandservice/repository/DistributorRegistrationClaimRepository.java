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
                    AND distributor_registration_claims.status IN ('RECEIVED', 'IN_PROGRESS')
                    ORDER BY distributor_registration_claims.creation_date DESC;
                    """,
            nativeQuery = true
    )
    List<DistributorRegistrationClaim> findAllRelevantByAdminId(@Param("adminId") Long adminId);

    @Query(
            value = """
                    SELECT * FROM distributor_registration_claims
                    WHERE distributor_registration_claims.admin_id = :adminId
                    AND distributor_registration_claims.status IN ('COMPLETED', 'DENIED')
                    ORDER BY distributor_registration_claims.creation_date DESC;
                    """,
            nativeQuery = true
    )
    List<DistributorRegistrationClaim> findAllIrrelevantByAdminId(@Param("adminId") Long adminId);


}
