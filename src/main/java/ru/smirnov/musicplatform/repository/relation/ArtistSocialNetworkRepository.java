package ru.smirnov.musicplatform.repository.relation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.relation.ArtistsSocialNetworks;

import java.util.List;

@Repository
public interface ArtistSocialNetworkRepository extends JpaRepository<ArtistsSocialNetworks, Long> {

    @Query(
            value = """
                    INSERT INTO artists_social_networks (artist_id, social_network, reference)
                    VALUES (:artistId, :socialNetworkName, :reference)
                    RETURNING id
                    """,
            nativeQuery = true
    )
    Long save(@Param("artistId") Long artistId, @Param("socialNetworkName") String socialNetworkName, @Param("reference") String reference);

    // создать соцсети: надо проверить, что для одного исполнителя уникальны соцсети (и не повторяются они)
    // ТОЛЬКО ЭТО НЕ ЗДЕСЬ: это в момент добавления

    List<ArtistsSocialNetworks> findAllByArtistId(Long artistId);

}
