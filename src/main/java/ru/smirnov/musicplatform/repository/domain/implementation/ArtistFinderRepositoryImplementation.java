package ru.smirnov.musicplatform.repository.domain.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.audience.Distributor;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.entity.relation.DistributorsByArtists;
import ru.smirnov.musicplatform.repository.domain.finder.ArtistFinderRepository;

import java.util.List;

@Repository
public class ArtistFinderRepositoryImplementation implements ArtistFinderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Artist> searchArtists(String searchRequest) {
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Artist> query = criteriaBuilder.createQuery(Artist.class);

        Root<Artist> artistRoot = query.from(Artist.class);

        Predicate artistNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(artistRoot.get("name")),
                "%" + searchRequest.toLowerCase() + "%"
        );

        query.select(artistRoot).where(artistNamePredicate);

        return this.entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Artist> searchArtists(String searchRequest, Long distributorId) {

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Artist> query = criteriaBuilder.createQuery(Artist.class);

        Root<Artist> artist = query.from(Artist.class);
        Join<Artist, DistributorsByArtists> distributorsByArtistsJoin = artist.join("relationWithDistributors", JoinType.INNER);

        Predicate distributorByArtistPredicate = criteriaBuilder.equal(
                distributorsByArtistsJoin.get("distributor").get("id"),
                distributorId
        );

        Predicate artistNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(artist.get("name")),
                "%" + searchRequest.toLowerCase() + "%"
        );

        query.select(artist).where(
                criteriaBuilder.and(
                        distributorByArtistPredicate,
                        artistNamePredicate
                )
        );

        return this.entityManager.createQuery(query).getResultList();
    }

}
