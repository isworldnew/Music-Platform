package ru.smirnov.demandservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smirnov.demandservice.entity.domain.TrackClaim;

@Repository
public interface TrackClaimRepository extends JpaRepository<TrackClaim, Long> {
}
