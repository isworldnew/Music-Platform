package ru.smirnov.musicplatform.repository.relation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.relation.DistributorsByArtists;

@Repository
public interface DistributorByArtistRepository extends JpaRepository<DistributorsByArtists, Long> {
}
