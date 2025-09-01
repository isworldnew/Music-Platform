package ru.smirnov.musicplatform.repository.domain.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.entity.relation.SavedAlbums;
import ru.smirnov.musicplatform.repository.domain.finder.AlbumFinderRepository;

import java.util.List;

@Repository
public class AlbumFinderRepositoryImplementation implements AlbumFinderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Album> searchAlbums(String searchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Album> criteriaQuery = criteriaBuilder.createQuery(Album.class);

        Root<Album> albumRoot = criteriaQuery.from(Album.class);

        Join<Album, Artist> albumJoinArtist = albumRoot.join("artist", JoinType.INNER);

        Predicate albumNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(albumRoot.get("name")),
                "%" + searchRequest.toLowerCase() + "%"
        );

        Predicate artistNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(albumJoinArtist.get("name")),
                "%" + searchRequest.toLowerCase() + "%"
        );

        Predicate finalPredicate = criteriaBuilder.or(albumNamePredicate, artistNamePredicate);

        return this.entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Album> searchSavedAlbums(String searchRequest, Long userId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Album> criteriaQuery = criteriaBuilder.createQuery(Album.class);

        Root<Album> albumRoot = criteriaQuery.from(Album.class);

        Join<Album, SavedAlbums> albumJoinSavedAlbums = albumRoot.join("savedBy", JoinType.INNER);
        Join<SavedAlbums, User> savedAlbumsJoinUser = albumJoinSavedAlbums.join("user", JoinType.INNER);

        Join<Album, Artist> albumJoinArtist = albumRoot.join("artist", JoinType.INNER); // ?

        Predicate userPredicate = criteriaBuilder.equal(savedAlbumsJoinUser.get("id"), userId); // ?

        Predicate albumNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(albumRoot.get("name")),
                "%" + searchRequest.toLowerCase() + "%"
        );

        Predicate artistNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(albumJoinArtist.get("name")),
                "%" + searchRequest.toLowerCase() + "%"
        );

        Predicate searchPredicate = criteriaBuilder.or(albumNamePredicate, artistNamePredicate);

        criteriaQuery.where(criteriaBuilder.and(searchPredicate, userPredicate)); // ?

        return this.entityManager.createQuery(criteriaQuery).getResultList();
    }
}
