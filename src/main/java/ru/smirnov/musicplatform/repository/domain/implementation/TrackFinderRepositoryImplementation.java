package ru.smirnov.musicplatform.repository.domain.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.auxiliary.enums.TrackStatus;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.entity.domain.Tag;
import ru.smirnov.musicplatform.entity.domain.Track;
import ru.smirnov.musicplatform.entity.relation.SavedTracks;
import ru.smirnov.musicplatform.entity.relation.TaggedTracks;
import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;
import ru.smirnov.musicplatform.projection.implementation.TrackShortcutProjectionImplementation;
import ru.smirnov.musicplatform.repository.domain.finder.TrackFinderRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    private List<TrackShortcutProjection> searchTracksGloballyGuest(String searchRequest) {

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

    private List<TrackShortcutProjection> searchTracksGloballyUser(String searchRequest, Long userId) {

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

    private List<TrackShortcutProjection> searchSavedTracks(String searchRequest, Long userId) {

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

    @Override
    public List<TrackShortcutProjection> searchTracksByTagsCombination(Set<Long> tagsId) {

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<TaggedTracks> query = criteriaBuilder.createQuery(TaggedTracks.class);

        Root<TaggedTracks> taggedTracks = query.from(TaggedTracks.class);

        Predicate recordsRelatedWithTagPredicate = taggedTracks.get("tag").get("id").in(tagsId);

        query.groupBy(taggedTracks.get("track").get("id"));

        query.having(
                criteriaBuilder.equal(
                    criteriaBuilder.countDistinct(taggedTracks.get("tag").get("id")),
                    tagsId.size()
                )
        );

        query.select(taggedTracks.get("track").get("id")).where(recordsRelatedWithTagPredicate);

        Set<Long> tracksId = this.entityManager.createQuery(query).getResultList().stream()
                .map(taggedTrack -> taggedTrack.getTrack().getId())
                .collect(Collectors.toSet());

        return this.findShortcutsById(tracksId);
    }

    private List<TrackShortcutProjection> findShortcutsById(Set<Long> tracksId) {

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<TrackShortcutProjection> query = criteriaBuilder.createQuery(TrackShortcutProjection.class);

        Root<Track> track = query.from(Track.class);
        Join<Track, Artist> artistJoin = track.join("artist", JoinType.INNER);

        Predicate trackIdPredicate = track.get("id").in(tracksId);

        query.where(trackIdPredicate);

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

        return this.entityManager.createQuery(query).getResultList();
    }
    
}
