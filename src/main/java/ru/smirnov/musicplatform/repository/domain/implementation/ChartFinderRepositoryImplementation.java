package ru.smirnov.musicplatform.repository.domain.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.audience.Admin;
import ru.smirnov.musicplatform.entity.auxiliary.enums.MusicCollectionAccessLevel;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.entity.domain.Chart;
import ru.smirnov.musicplatform.entity.relation.SavedCharts;
import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;
import ru.smirnov.musicplatform.projection.implementation.MusicCollectionShortcutProjectionImplementation;
import ru.smirnov.musicplatform.repository.domain.finder.ChartFinderRepository;

import java.util.List;

@Repository
public class ChartFinderRepositoryImplementation implements ChartFinderRepository {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MusicCollectionShortcutProjection> searchCharts(String searchRequest, Long userId, boolean savedOnly) {

        if (userId == null && savedOnly)
            throw new IllegalStateException("'savedOnly' flag can only be used with not-null userId");

        if (userId == null)
            return this.searchChartsGloballyGuest(searchRequest);

        else if (savedOnly) return this.searchChartsGloballyAdmin(searchRequest, userId);

        else return this.searchSavedCharts(searchRequest, userId);
    }
    
    private List<MusicCollectionShortcutProjection> searchChartsGloballyGuest(String searchRequest) {

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MusicCollectionShortcutProjection> query = criteriaBuilder.createQuery(MusicCollectionShortcutProjection.class);

        Root<Chart> chart = query.from(Chart.class);
        Join<Chart, Artist> artistJoin = chart.join("artist", JoinType.INNER);
        Join<Chart, Admin> adminJoin = chart.join("admin", JoinType.INNER);

        query.select(criteriaBuilder.construct(
                MusicCollectionShortcutProjectionImplementation.class,
                chart.get("id"),
                chart.get("name"),
                chart.get("imageReference"),
                adminJoin.get("id"),
                adminJoin.get("account").get("username"),
                chart.get("accessLevel"),
                criteriaBuilder.nullLiteral(Boolean.class)
        ));

        Predicate accessLevelPredicate = criteriaBuilder.equal(
                chart.get("accessLevel"),
                MusicCollectionAccessLevel.PUBLIC
        );

        Predicate namePredicate = criteriaBuilder.or(
                criteriaBuilder.like(
                        criteriaBuilder.lower(chart.get("name")),
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

    private List<MusicCollectionShortcutProjection> searchChartsGloballyAdmin(String searchRequest, Long userId) {

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MusicCollectionShortcutProjection> query = criteriaBuilder.createQuery(MusicCollectionShortcutProjection.class);

        Root<Chart> chart = query.from(Chart.class);
        Join<Chart, Artist> artistJoin = chart.join("artist", JoinType.INNER);
        Join<Chart, Admin> adminJoin = chart.join("admin", JoinType.INNER);
        Join<Chart, SavedCharts> savedChartsJoin = chart.join("savedBy", JoinType.LEFT);

        Expression<Object> savedExpression = criteriaBuilder.selectCase()
                        .when(criteriaBuilder.equal(savedChartsJoin.get("user").get("id"), userId), true)
                        .otherwise(false);

        query.select(criteriaBuilder.construct(
                MusicCollectionShortcutProjectionImplementation.class,
                chart.get("id"),
                chart.get("name"),
                chart.get("imageReference"),
                adminJoin.get("id"),
                adminJoin.get("account").get("username"),
                chart.get("accessLevel"),
                savedExpression
        ));

        Predicate savedPredicate = criteriaBuilder.equal(
                savedChartsJoin.get("user").get("id"),
                userId
        );

        Predicate accessLevelPredicate = criteriaBuilder.equal(
                chart.get("accessLevel"),
                MusicCollectionAccessLevel.PUBLIC
        );

        Predicate namePredicate = criteriaBuilder.or(
                criteriaBuilder.like(
                        criteriaBuilder.lower(chart.get("name")),
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

    private List<MusicCollectionShortcutProjection> searchSavedCharts(String searchRequest, Long userId) {

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MusicCollectionShortcutProjection> query = criteriaBuilder.createQuery(MusicCollectionShortcutProjection.class);

        Root<Chart> chart = query.from(Chart.class);
        Join<Chart, Artist> artistJoin = chart.join("artist", JoinType.INNER);
        Join<Chart, Admin> adminJoin = chart.join("admin", JoinType.INNER);
        Join<Chart, SavedCharts> savedChartsJoin = chart.join("savedBy", JoinType.LEFT);

        query.select(criteriaBuilder.construct(
                MusicCollectionShortcutProjectionImplementation.class,
                chart.get("id"),
                chart.get("name"),
                chart.get("imageReference"),
                adminJoin.get("id"),
                adminJoin.get("account").get("username"),
                chart.get("accessLevel"),
                criteriaBuilder.literal(true)
        ));

        Predicate savedPredicate = criteriaBuilder.equal(
                savedChartsJoin.get("user").get("id"),
                userId
        );

        Predicate namePredicate = criteriaBuilder.or(
                criteriaBuilder.like(
                        criteriaBuilder.lower(chart.get("name")),
                        "%" + searchRequest + "%"
                ),
                criteriaBuilder.like(
                        criteriaBuilder.lower(artistJoin.get("name")),
                        "%" + searchRequest + "%"
                )
        );

        query.where(criteriaBuilder.and(savedPredicate, namePredicate));

        return this.entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<MusicCollectionShortcutProjection> searchChartsByAdmin(String searchRequest, Long adminId) {

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MusicCollectionShortcutProjection> query = criteriaBuilder.createQuery(MusicCollectionShortcutProjection.class);

        Root<Chart> chart = query.from(Chart.class);
        Join<Chart, Admin> adminJoin = chart.join("admin", JoinType.INNER);

        query.select(criteriaBuilder.construct(
                MusicCollectionShortcutProjectionImplementation.class,
                chart.get("id"),
                chart.get("name"),
                chart.get("imageReference"),
                adminJoin.get("id"),
                adminJoin.get("account").get("username"),
                chart.get("accessLevel"),
                criteriaBuilder.nullLiteral(Boolean.class)
        ));

        Predicate chartByAdminPredicate = criteriaBuilder.equal(
                adminJoin.get("id"),
                adminId
        );

        Predicate namePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(chart.get("name")),
                "%" + searchRequest.toLowerCase() + "%"
        );

        Predicate finalPredicate = criteriaBuilder.and(chartByAdminPredicate, namePredicate);

        query.where(finalPredicate);

        return this.entityManager.createQuery(query).getResultList();
    }

}
