package ru.smirnov.musicplatform.repository.domain.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistShortcutResponse;
import ru.smirnov.musicplatform.entity.domain.Artist;
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
                "%" + searchRequest + "%"
        );

        query.select(artistRoot).where(artistNamePredicate);

        return this.entityManager.createQuery(query).getResultList();
    }

}
