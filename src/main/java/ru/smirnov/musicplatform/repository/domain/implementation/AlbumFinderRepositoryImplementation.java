package ru.smirnov.musicplatform.repository.domain.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.auxiliary.enums.MusicCollectionAccessLevel;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.entity.relation.SavedAlbums;
import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;
import ru.smirnov.musicplatform.projection.implementation.MusicCollectionShortcutProjectionImplementation;
import ru.smirnov.musicplatform.repository.domain.finder.AlbumFinderRepository;

import java.util.List;

@Repository
public class AlbumFinderRepositoryImplementation implements AlbumFinderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MusicCollectionShortcutProjection> searchAlbums(String searchRequest, Long userId, boolean savedOnly) {
        if (userId == null && savedOnly)
            throw new IllegalStateException("'savedOnly' flag can only be used with not-null userId");

        // глобальный поиск среди альбомов для гостя (только PUBLIC-альбомы и без информации о сохранении)
        if (userId == null) return this.searchAlbumsGloballyGuest(searchRequest);

        // глобальный поиск среди альбомов для пользователя (получим все PUBLIC-альбомы + альбомы любого уровня доступа, но сохранённые)
        // userId != null && !savedOnly
        else if (!savedOnly) return this.searchAlbumsGloballyUser(searchRequest, userId);

        // поиск среди сохранённых альбомов пользователя (в результате могут быть альбомы любого уровня доступа)
        // userId != null && savedOnly
        else return this.searchSavedAlbums(searchRequest, userId);
    }

    private List<MusicCollectionShortcutProjection> searchAlbumsGloballyGuest(String searchRequest) {

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MusicCollectionShortcutProjection> query = criteriaBuilder.createQuery(MusicCollectionShortcutProjection.class);

        Root<Album> album = query.from(Album.class);
        Join<Album, Artist> artistJoin = album.join("artist", JoinType.INNER);

        query.select(criteriaBuilder.construct(
                MusicCollectionShortcutProjectionImplementation.class,
                album.get("id"),
                album.get("name"),
                album.get("imageReference"),
                artistJoin.get("id"),
                artistJoin.get("name"),
                album.get("accessLevel"),
                criteriaBuilder.nullLiteral(Boolean.class)
        ));

        Predicate accessLevelPredicate = criteriaBuilder.equal(
                album.get("accessLevel"),
                MusicCollectionAccessLevel.PUBLIC
        );

        Predicate namePredicate = criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(album.get("name")), "%" + searchRequest.toLowerCase() + "%"),
                criteriaBuilder.like(criteriaBuilder.lower(artistJoin.get("name")), "%" + searchRequest.toLowerCase() + "%")
        );

        Predicate finalPredicate = criteriaBuilder.and(accessLevelPredicate, namePredicate);

        query.where(finalPredicate);

        TypedQuery<MusicCollectionShortcutProjection> albums = entityManager.createQuery(query);
        return albums.getResultList();
    }

    private List<MusicCollectionShortcutProjection> searchAlbumsGloballyUser(String searchRequest, Long userId) {

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MusicCollectionShortcutProjection> query = criteriaBuilder.createQuery(MusicCollectionShortcutProjection.class);

        Root<Album> album = query.from(Album.class);
        Join<Album, Artist> artistJoin = album.join("artist", JoinType.INNER);
        Join<Album, SavedAlbums> savedAlbumsJoin = album.join("savedBy", JoinType.LEFT);

        Expression<Object> savedExpression = criteriaBuilder.selectCase()
                .when(criteriaBuilder.equal(savedAlbumsJoin.get("user").get("id"), userId), true)
                .otherwise(false);

        query.select(criteriaBuilder.construct(
                MusicCollectionShortcutProjectionImplementation.class,
                album.get("id"),
                album.get("name"),
                album.get("imageReference"),
                artistJoin.get("id"),
                artistJoin.get("name"),
                album.get("accessLevel"),
                savedExpression
        ));

        Predicate accessLevelPredicate = criteriaBuilder.equal(
                album.get("accessLevel"),
                MusicCollectionAccessLevel.PUBLIC
        );

        Predicate savedPredicate = criteriaBuilder.equal(
                savedAlbumsJoin.get("user").get("id"),
                userId
        );

        Predicate namePredicate = criteriaBuilder.or(
                criteriaBuilder.like(
                        criteriaBuilder.lower(album.get("name")),
                        "%" + searchRequest.toLowerCase() + "%"
                ),
                criteriaBuilder.like(
                        criteriaBuilder.lower(artistJoin.get("name")),
                        "%" + searchRequest.toLowerCase() + "%"
                )
        );

        Predicate finalPredicate = criteriaBuilder.and(
                criteriaBuilder.or(accessLevelPredicate, savedPredicate),
                namePredicate
        );

        query.where(finalPredicate);

        return entityManager.createQuery(query).getResultList();
    }

    private List<MusicCollectionShortcutProjection> searchSavedAlbums(String searchRequest, Long userId) {

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MusicCollectionShortcutProjection> query = criteriaBuilder.createQuery(MusicCollectionShortcutProjection.class);

        Root<Album> album = query.from(Album.class);
        Join<Album, Artist> artistJoin = album.join("artist", JoinType.INNER);
        Join<Album, SavedAlbums> savedAlbumsJoin = album.join("savedBy", JoinType.INNER);

        query.select(criteriaBuilder.construct(
                MusicCollectionShortcutProjectionImplementation.class,
                album.get("id"),
                album.get("name"),
                album.get("imageReference"),
                artistJoin.get("id"),
                artistJoin.get("name"),
                album.get("accessLevel"),
                criteriaBuilder.literal(true)
        ));

        Predicate namePredicate = criteriaBuilder.or(
                criteriaBuilder.like(
                        criteriaBuilder.lower(album.get("name")),
                        "%" + searchRequest.toLowerCase() + "%"
                ),
                criteriaBuilder.like(
                        criteriaBuilder.lower(artistJoin.get("name")),
                        "%" + searchRequest.toLowerCase() + "%"
                )
        );

        Predicate savedPredicate = criteriaBuilder.equal(
                savedAlbumsJoin.get("user").get("id"),
                userId
        );

        query.where(criteriaBuilder.and(namePredicate, savedPredicate));

        TypedQuery<MusicCollectionShortcutProjection> albums = this.entityManager.createQuery(query);
        return albums.getResultList();
    }

    /*
    private Map<Album, Boolean> searchAlbumsGloballyGuest(String searchRequest) {

        // тут гарантированно все - SAVED = NULL

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Album> query = criteriaBuilder.createQuery(Album.class);

        Root<Album> albumRoot = query.from(Album.class);

        Join<Album, Artist> albumArtistJoin = albumRoot.join("artist", JoinType.INNER);

        Predicate accessLevelPredicate = criteriaBuilder.equal(
                albumRoot.get("accessLevel"),
                MusicCollectionAccessLevel.PUBLIC
        );

        Predicate albumNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(albumRoot.get("name")),
                "%" + searchRequest.toLowerCase() + "%"
        );

        Predicate artistNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(albumArtistJoin.get("name")),
                "%" + searchRequest.toLowerCase() + "%"
        );

        Predicate finalPredicate = criteriaBuilder.and(
                accessLevelPredicate,
                criteriaBuilder.or(albumNamePredicate, artistNamePredicate)
        );

        query.where(finalPredicate);

        List<Album> albums = this.entityManager.createQuery(query).getResultList();

        Map<Album, Boolean> albumsWithSavedInfo = new HashMap<>();

        albums.forEach(album -> albumsWithSavedInfo.put(album, null));

        return albumsWithSavedInfo;
    }

    private Map<Album, Boolean> searchAlbumsGloballyUser(String searchRequest, Long userId) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Album> query = criteriaBuilder.createQuery(Album.class);

        Root<Album> albumRoot = query.from(Album.class);
        Join<Album, SavedAlbums> savedAlbumsJoin = albumRoot.join("savedBy", JoinType.LEFT);

        Predicate publicAlbumPredicate = criteriaBuilder.equal(
            albumRoot.get("accessLevel"),
            MusicCollectionAccessLevel.PUBLIC
        );

        Predicate savedByUserPredicate = criteriaBuilder.and(
                criteriaBuilder.notEqual(
                        albumRoot.get("accessLevel"),
                        MusicCollectionAccessLevel.PUBLIC
                ),
                criteriaBuilder.equal(savedAlbumsJoin.get("user").get("id"), userId)
        );

        Predicate albumNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(albumRoot.get("name")),
                "%" + searchRequest.toLowerCase() + "%"
        );

        Predicate artistNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(albumRoot.join("artist", JoinType.INNER).get("name")),
                "%" + searchRequest.toLowerCase() + "%"
        );

        Predicate finalPredicate = criteriaBuilder.and(
                criteriaBuilder.or(publicAlbumPredicate, savedByUserPredicate),
                criteriaBuilder.or(albumNamePredicate, artistNamePredicate)
        );

        query.where(finalPredicate);
        query.distinct(true);

        List<Album> albums = this.entityManager.createQuery(query).getResultList();

        Map<Album, Boolean> albumsWithSavedInfo = new HashMap<>();

        albums.forEach(
                album -> albumsWithSavedInfo.put(
                        album,
                        album.getSavedBy().stream()
                                .map(savedAlbum -> savedAlbum.getUser().getId())
                                .toList().contains(userId)
                )
        );

        return albumsWithSavedInfo;
    }

    private Map<Album, Boolean> searchSavedAlbums(String searchRequest, Long userId) {

        // тут гарантированно все - SAVED

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Album> query = criteriaBuilder.createQuery(Album.class);

        Root<Album> albumRoot = query.from(Album.class);

        Join<Album, SavedAlbums> savedAlbumsJoin = albumRoot.join("savedBy", JoinType.INNER);
        Join<Album, Artist> artistJoin = albumRoot.join("artist", JoinType.INNER);

        Predicate albumNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(albumRoot.get("name")),
                "%" + searchRequest.toLowerCase() + "%"
        );

        Predicate artistNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(artistJoin.get("name")),
                "%" + searchRequest.toLowerCase() + "%"
        );

        Predicate albumIsSaved = criteriaBuilder.equal(savedAlbumsJoin.get("user").get("id"), userId);

        Predicate finalPredicate = criteriaBuilder.and(
                criteriaBuilder.or(albumNamePredicate, artistNamePredicate),
                albumIsSaved
        );

        query.where(finalPredicate);

        List<Album> albums = this.entityManager.createQuery(query).getResultList();

        Map<Album, Boolean> albumsWithSavedInfo = new HashMap<>();

        albums.forEach(album -> albumsWithSavedInfo.put(album, true));

        return albumsWithSavedInfo;
    }
    */
}
