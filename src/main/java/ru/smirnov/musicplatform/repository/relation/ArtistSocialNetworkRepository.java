package ru.smirnov.musicplatform.repository.relation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.relation.ArtistsSocialNetworks;

@Repository
public interface ArtistSocialNetworkRepository extends JpaRepository<ArtistsSocialNetworks, Long> {
}
