package ru.smirnov.musicplatform.repository.domain.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.entity.auxiliary.enums.MusicCollectionAccessLevel;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.entity.domain.Playlist;
import ru.smirnov.musicplatform.entity.relation.SavedPlaylists;
import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;
import ru.smirnov.musicplatform.projection.implementation.MusicCollectionShortcutProjectionImplementation;
import ru.smirnov.musicplatform.repository.domain.finder.PlaylistFinderRepository;

import java.util.List;

@Repository
public class PlaylistFinderRepositoryImplementation implements PlaylistFinderRepository {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MusicCollectionShortcutProjection> searchPlaylists(String searchRequest, Long userId, boolean savedOnly) {
        
        if (userId == null && savedOnly)
            throw new IllegalStateException("'savedOnly' flag can only be used with not-null userId");
        
        if (userId == null)
            return this.searchPlaylistsGloballyGuest(searchRequest);
        
        else if (savedOnly) return this.searchPlaylistsGloballyUser(searchRequest, userId);
        
        else return this.searchSavedPlaylists(searchRequest, userId);
        
    }
    
    private List<MusicCollectionShortcutProjection> searchPlaylistsGloballyGuest(String searchRequest) {
        
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MusicCollectionShortcutProjection> query = criteriaBuilder.createQuery(MusicCollectionShortcutProjection.class);

        Root<Playlist> playlist = query.from(Playlist.class);
        Join<Playlist, Artist> artistJoin = playlist.join("artist", JoinType.INNER);
        Join<Playlist, User> userJoin = playlist.join("user", JoinType.INNER);

        query.select(criteriaBuilder.construct(
                MusicCollectionShortcutProjectionImplementation.class,
                playlist.get("id"),
                playlist.get("name"),
                playlist.get("imageReference"),
                userJoin.get("id"),
                userJoin.get("account").get("username"),
                playlist.get("accessLevel"),
                criteriaBuilder.nullLiteral(Boolean.class)
        ));

        Predicate accessLevelPredicate = criteriaBuilder.equal(
                playlist.get("accessLevel"),
                MusicCollectionAccessLevel.PUBLIC
        );

        Predicate namePredicate = criteriaBuilder.or(
                criteriaBuilder.like(
                        criteriaBuilder.lower(playlist.get("name")),
                        "%" + searchRequest + "%"
                ),
                criteriaBuilder.like(
                        criteriaBuilder.lower(artistJoin.get("name")),
                        "%" + searchRequest + "%"
                )
        );

        Predicate finalPredicate = criteriaBuilder.and(accessLevelPredicate, namePredicate);

        query.where(finalPredicate);

        return this.entityManager.createQuery(query).getResultList();
    }

    private List<MusicCollectionShortcutProjection> searchPlaylistsGloballyUser(String searchRequest, Long userId) {

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MusicCollectionShortcutProjection> query = criteriaBuilder.createQuery(MusicCollectionShortcutProjection.class);

        Root<Playlist> playlist = query.from(Playlist.class);
        Join<Playlist, Artist> artistJoin = playlist.join("artist", JoinType.INNER);
        Join<Playlist, User> userJoin = playlist.join("user", JoinType.INNER);
        Join<Playlist, SavedPlaylists> savedPlaylistsJoin = playlist.join("savedBy", JoinType.LEFT);

        Expression<Object> savedExpression = criteriaBuilder.selectCase()
                .when(criteriaBuilder.equal(savedPlaylistsJoin.get("user").get("id"), userId), true)
                .otherwise(false);

        query.select(criteriaBuilder.construct(
                MusicCollectionShortcutProjectionImplementation.class,
                playlist.get("id"),
                playlist.get("name"),
                playlist.get("imageReference"),
                userJoin.get("id"),
                userJoin.get("account").get("username"),
                playlist.get("accessLevel"),
                savedExpression
        ));

        Predicate accessLevelPredicate = criteriaBuilder.equal(
                playlist.get("accessLevel"),
                MusicCollectionAccessLevel.PUBLIC
        );

        Predicate savedPredicate = criteriaBuilder.equal(
                userJoin.get("user").get("id"),
                userId
        );

        Predicate namePredicate = criteriaBuilder.or(
                criteriaBuilder.like(
                        criteriaBuilder.lower(playlist.get("name")),
                        "%" + searchRequest + "%"
                ),
                criteriaBuilder.like(
                        criteriaBuilder.lower(artistJoin.get("name")),
                        "%" + searchRequest + "%"
                )
        );

        Predicate finalPredicate = criteriaBuilder.and(
                criteriaBuilder.or(savedPredicate, accessLevelPredicate),
                namePredicate
        );

        query.where(finalPredicate);

        return this.entityManager.createQuery(query).getResultList();
    }

    private List<MusicCollectionShortcutProjection> searchSavedPlaylists(String searchRequest, Long userId) {

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MusicCollectionShortcutProjection> query = criteriaBuilder.createQuery(MusicCollectionShortcutProjection.class);

        Root<Playlist> playlist = query.from(Playlist.class);
        Join<Playlist, Artist> artistJoin = playlist.join("artist", JoinType.INNER);
        Join<Playlist, User> userJoin = playlist.join("user", JoinType.INNER);
        Join<Playlist, SavedPlaylists> savedPlaylistsJoin = playlist.join("savedBy", JoinType.INNER);

        query.select(criteriaBuilder.construct(
                MusicCollectionShortcutProjectionImplementation.class,
                playlist.get("id"),
                playlist.get("name"),
                playlist.get("imageReference"),
                userJoin.get("id"),
                userJoin.get("account").get("username"),
                playlist.get("accessLevel"),
                criteriaBuilder.literal(true)
        ));

        Predicate savedPredicate = criteriaBuilder.equal(
                savedPlaylistsJoin.get("user").get("id"),
                userId
        );

        Predicate namePredicate = criteriaBuilder.or(
                criteriaBuilder.like(
                        criteriaBuilder.lower(playlist.get("name")),
                        "%" + searchRequest + "%"
                ),
                criteriaBuilder.like(
                        criteriaBuilder.lower(artistJoin.get("name")),
                        "%" + searchRequest + "%"
                )
        );

        Predicate finalPredicate = criteriaBuilder.and(savedPredicate, namePredicate);

        query.where(finalPredicate);

        return this.entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<MusicCollectionShortcutProjection> getOwnedPlaylists(Long userId) {

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MusicCollectionShortcutProjection> query = criteriaBuilder.createQuery(MusicCollectionShortcutProjection.class);

        Root<Playlist> playlist = query.from(Playlist.class);
        Join<Playlist, User> userJoin = playlist.join("user", JoinType.INNER);

        query.select(criteriaBuilder.construct(
                MusicCollectionShortcutProjectionImplementation.class,
                playlist.get("id"),
                playlist.get("name"),
                playlist.get("imageReference"),
                userJoin.get("id"),
                userJoin.get("account").get("username"),
                playlist.get("accessLevel"),
                criteriaBuilder.nullLiteral(Boolean.class)
        ));

        Predicate belongsToUserPredicate = criteriaBuilder.equal(
                userJoin.get("id"), userId
        );

        query.where(belongsToUserPredicate);

        return this.entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<MusicCollectionShortcutProjection> getSavedPlaylists(Long userId) {

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MusicCollectionShortcutProjection> query = criteriaBuilder.createQuery(MusicCollectionShortcutProjection.class);

        Root<Playlist> playlist = query.from(Playlist.class);
        Join<Playlist, User> userJoin = playlist.join("user", JoinType.INNER);
        Join<Playlist, SavedPlaylists> savedPlaylistsJoin = playlist.join("savedBy", JoinType.INNER);

        query.select(criteriaBuilder.construct(
                MusicCollectionShortcutProjectionImplementation.class,
                playlist.get("id"),
                playlist.get("name"),
                playlist.get("imageReference"),
                userJoin.get("id"),
                userJoin.get("account").get("username"),
                playlist.get("accessLevel"),
                criteriaBuilder.literal(true)
        ));

        Predicate savedByUserPredicate = criteriaBuilder.equal(
                savedPlaylistsJoin.get("user").get("id"),
                userId
        );

        query.where(savedByUserPredicate);

        return this.entityManager.createQuery(query).getResultList();
    }
}
