package ru.smirnov.musicplatform.repository.domain.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.auxiliary.enums.TrackStatus;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.entity.domain.Track;
import ru.smirnov.musicplatform.entity.relation.SavedTracks;
import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;
import ru.smirnov.musicplatform.projection.implementation.TrackShortcutProjectionImplementation;
import ru.smirnov.musicplatform.repository.domain.finder.TrackFinderRepository;

import java.util.List;

@Repository
public class TrackFinderRepositoryImplementation implements TrackFinderRepository {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TrackShortcutProjection> searchTracks(String searchRequest, Long userId, boolean savedOnly) {
        if (userId == null && savedOnly)
            throw new IllegalStateException("'savedOnly' flag can only be used with not-null userId");

        // глобальный поиск среди треков для гостя (только PUBLIC-треки и без информации о сохранении)
        if (userId == null) return this.searchTracksGloballyGuest(searchRequest);

            // глобальный поиск среди треков для пользователя (получим все PUBLIC-треки + треки любого уровня доступа, но сохранённые)
            // userId != null && !savedOnly
        else if (!savedOnly) return this.searchTracksGloballyUser(searchRequest, userId);

            // поиск среди сохранённых треков пользователя (в результате могут быть треки любого уровня доступа)
            // userId != null && savedOnly
        else return this.searchSavedTracks(searchRequest, userId);
    }

    public List<TrackShortcutProjection> searchTracksGloballyGuest(String searchRequest) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TrackShortcutProjection> query = criteriaBuilder.createQuery(TrackShortcutProjection.class);

        Root<Track> track = query.from(Track.class);

        Join<Track, Artist> artistJoin = track.join("artist", JoinType.INNER);

        query.select(criteriaBuilder.construct(
                TrackShortcutProjectionImplementation.class,
                track.get("id"),
                track.get("name"),
                artistJoin.get("id"),
                artistJoin.get("name"),
                track.get("status"),
                track.get("imageReference"),
                criteriaBuilder.nullLiteral(Boolean.class)
        ));

        Predicate accessLevelCondition = criteriaBuilder.equal(
                track.get("status"),
                TrackStatus.PUBLISHED
        );

        Predicate nameCondition = criteriaBuilder.or(
                criteriaBuilder.like(
                        criteriaBuilder.lower(track.get("name")),
                        "%" + searchRequest.toLowerCase() + "%"
                ),
                criteriaBuilder.like(
                        criteriaBuilder.lower(artistJoin.get("name")),
                        "%" + searchRequest.toLowerCase() + "%"
                )
        );

        query.where(
                criteriaBuilder.and(
                        accessLevelCondition,
                        nameCondition
                )
        );

        TypedQuery<TrackShortcutProjection> tracks = entityManager.createQuery(query);
        return tracks.getResultList();
    }

    public List<TrackShortcutProjection> searchTracksGloballyUser(String searchRequest, Long userId) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TrackShortcutProjection> query = criteriaBuilder.createQuery(TrackShortcutProjection.class);

        Root<Track> track = query.from(Track.class);
        Join<Track, Artist> artistJoin = track.join("artist", JoinType.INNER);
        Join<Track, SavedTracks> savedTracksJoin = track.join("savedBy", JoinType.LEFT);

        Expression<Object> savedExpression = criteriaBuilder.selectCase()
                .when(criteriaBuilder.equal(savedTracksJoin.get("user").get("id"), userId), true)
                .otherwise(false);

        query.select(criteriaBuilder.construct(
                TrackShortcutProjectionImplementation.class,
                track.get("id"),
                track.get("name"),
                artistJoin.get("id"),
                artistJoin.get("name"),
                track.get("status"),
                track.get("imageReference"),
                savedExpression
        ));

        Predicate statusPredicate = criteriaBuilder.equal(
                track.get("status"),
                TrackStatus.PUBLISHED
        );

        Predicate savedPredicate = criteriaBuilder.equal(
                savedTracksJoin.get("user").get("id"),
                userId
        );

        Predicate namePredicate = criteriaBuilder.or(
                criteriaBuilder.like(
                        criteriaBuilder.lower(track.get("name")),
                        "%" + searchRequest.toLowerCase() + "%"
                ),
                criteriaBuilder.like(
                        criteriaBuilder.lower(artistJoin.get("name")),
                        "%" + searchRequest.toLowerCase() + "%"
                )
        );

        query.where(
                criteriaBuilder.and(
                        criteriaBuilder.or(savedPredicate, statusPredicate),
                        namePredicate
                )
        );

        TypedQuery<TrackShortcutProjection> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

    public List<TrackShortcutProjection> searchSavedTracks(String searchRequest, Long userId) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TrackShortcutProjection> query = criteriaBuilder.createQuery(TrackShortcutProjection.class);

        Root<Track> track = query.from(Track.class);
        Join<Track, Artist> artistJoin = track.join("artist", JoinType.INNER);
        Join<Track, SavedTracks> savedTracksJoin = track.join("savedBy", JoinType.INNER);

        query.select(criteriaBuilder.construct(
                TrackShortcutProjectionImplementation.class,
                track.get("id"),
                track.get("name"),
                artistJoin.get("id"),
                artistJoin.get("name"),
                track.get("status"),
                track.get("imageReference"),
                criteriaBuilder.literal(true)

        ));

        Predicate savedPredicate = criteriaBuilder.equal(
                savedTracksJoin.get("user").get("id"),
                userId
        );

        Predicate namePredicate = criteriaBuilder.or(
                criteriaBuilder.like(
                        criteriaBuilder.lower(track.get("name")),
                        "%" + searchRequest.toLowerCase() + "%"
                ),
                criteriaBuilder.like(
                        criteriaBuilder.lower(artistJoin.get("name")),
                        "%" + searchRequest.toLowerCase() + "%"
                )
        );

        query.where(
                criteriaBuilder.and(
                        savedPredicate,
                        namePredicate
                )
        );

        TypedQuery<TrackShortcutProjection> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }
    /*
    private Map<Track, Boolean> searchTracksGloballyGuest(String searchRequest) {

        // тут гарантированно все - SAVED = NULL

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Track> query = criteriaBuilder.createQuery(Track.class);

        Root<Track> trackRoot = query.from(Track.class);
        Join<Track, Artist> artistJoin = trackRoot.join("artist", JoinType.INNER);
        Join<Track, CoArtists> coArtistsJoin = trackRoot.join("coArtists", JoinType.LEFT);

        Predicate trackStatus = criteriaBuilder.equal(
                trackRoot.get("status"),
                TrackStatus.PUBLISHED
        );

        Predicate trackNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(trackRoot.get("name")),
                "%" + searchRequest.toLowerCase() + "%"
        );

        Predicate artistNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(artistJoin.get("name")),
                "%" + searchRequest.toLowerCase() + "%"
        );

        Predicate coArtistNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(coArtistsJoin.get("artist").get("name")),
                "%" + searchRequest.toLowerCase() + "%"
        );

        Predicate namePredicate = criteriaBuilder.or(
                trackNamePredicate,
                artistNamePredicate,
                coArtistNamePredicate
        );

        Predicate finalPredicate = criteriaBuilder.and(trackStatus, namePredicate);

        query.select(trackRoot).where(finalPredicate);

        List<Track> tracks = this.entityManager.createQuery(query).getResultList();

        System.out.println(tracks);

        Map<Track, Boolean> tracksWithSavedInfo = new HashMap<>();

        tracks.forEach(track -> tracksWithSavedInfo.put(track, null));

        return tracksWithSavedInfo;
    }

    private Map<Track, Boolean> searchTracksGloballyUser(String searchRequest, Long userId) {

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Track> query = criteriaBuilder.createQuery(Track.class);

        Root<Track> trackRoot = query.from(Track.class);
        Join<Track, Artist> artistJoin = trackRoot.join("artist", JoinType.INNER);
        Join<Track, CoArtists> coArtistsJoin = trackRoot.join("coArtists", JoinType.LEFT);
        Join<Track, SavedTracks> savedTracksJoin = trackRoot.join("savedBy", JoinType.LEFT);

        // трек сохранён или PUBLIC
        Predicate trackIsPublic = criteriaBuilder.equal(
                trackRoot.get("status"),
                TrackStatus.PUBLISHED
        );

        Predicate trackNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(trackRoot.get("name")),
                "%" + searchRequest.toLowerCase() + "%"
        );

        Predicate artistNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(artistJoin.get("name")),
                "%" + searchRequest.toLowerCase() + "%"
        );

        Predicate coArtistNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(coArtistsJoin.get("artist").get("name")),
                "%" + searchRequest.toLowerCase() + "%"
        );

        Predicate trackIsSaved = criteriaBuilder.equal(savedTracksJoin.get("user").get("id"), userId);

        Predicate namePredicate = criteriaBuilder.or(
                trackNamePredicate,
                artistNamePredicate,
                coArtistNamePredicate
        );

        Predicate finalPredicate = criteriaBuilder.and(
                criteriaBuilder.or(trackIsSaved, trackIsPublic),
                namePredicate
        );

        query.select(trackRoot).distinct(true).where(finalPredicate);

        List<Track> tracks = this.entityManager.createQuery(query).getResultList();

        Map<Track, Boolean> tracksWithSavedInfo = new HashMap<>();

        tracks.forEach(
                track -> tracksWithSavedInfo.put(
                        track,
                        track.getSavedBy().stream()
                                .map(savedTrack -> savedTrack.getUser().getId())
                                .toList()
                                .contains(userId)
                )
        );

        return tracksWithSavedInfo;
    }

    private Map<Track, Boolean> searchSavedTracks(String searchRequest, Long userId) {

        // тут гарантированно все - SAVED

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Track> query = criteriaBuilder.createQuery(Track.class);

        Root<Track> trackRoot = query.from(Track.class);
        Join<Track, Artist> artistJoin = trackRoot.join("artist", JoinType.INNER);
        Join<Track, CoArtists> coArtistsJoin = trackRoot.join("coArtists", JoinType.LEFT);
        Join<Track, SavedTracks> savedTracksJoin = trackRoot.join("savedBy", JoinType.INNER);

        Predicate trackNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(trackRoot.get("name")),
                "%" + searchRequest.toLowerCase() + "%"
        );

        Predicate artistNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(artistJoin.get("name")),
                "%" + searchRequest.toLowerCase() + "%"
        );

        Predicate coArtistNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(coArtistsJoin.get("artist").get("name")),
                "%" + searchRequest.toLowerCase() + "%"
        );

        Predicate trackIsSaved = criteriaBuilder.equal(savedTracksJoin.get("user").get("id"), userId);

        Predicate namePredicate = criteriaBuilder.or(
                trackNamePredicate,
                artistNamePredicate,
                coArtistNamePredicate
        );

        Predicate finalPredicate = criteriaBuilder.and(trackIsSaved, namePredicate);

        query.select(trackRoot).distinct(true).where(finalPredicate);

        List<Track> tracks = this.entityManager.createQuery(query).getResultList();

        Map<Track, Boolean> tracksWithSavedInfo = new HashMap<>();

        tracks.forEach(track -> tracksWithSavedInfo.put(track, true));

        return tracksWithSavedInfo;
    }
    */
}
